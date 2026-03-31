# VendorVault CLM - Maintenance Guide

## Daily Operations

### Health Checks
```bash
# Check application status
curl http://localhost:8080/actuator/health

# Check database connection
mysql -u vendorvault_user -p -e "SELECT 1"

# Check disk space
df -h

# Check memory usage
free -m

# Check application logs
tail -f logs/application.log
```

### Monitoring Checklist
- [ ] Application is running
- [ ] Database is accessible
- [ ] No critical errors in logs
- [ ] Response time < 2 seconds
- [ ] Disk space > 20% free
- [ ] Memory usage < 80%

## Weekly Tasks

### 1. Review Logs
```bash
# Check for errors
grep -i "error" logs/application.log | tail -50

# Check for warnings
grep -i "warn" logs/application.log | tail -50

# Check failed login attempts
grep -i "failed login" logs/application.log
```

### 2. Database Maintenance
```sql
-- Check table sizes
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS "Size (MB)"
FROM information_schema.TABLES
WHERE table_schema = 'vendorvault_db'
ORDER BY (data_length + index_length) DESC;

-- Check for locked accounts
SELECT email, failed_login_attempts, account_locked 
FROM users 
WHERE account_locked = true;

-- Check expiring contracts (next 30 days)
SELECT contract_number, title, end_date 
FROM contracts 
WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
AND status = 'ACTIVE';
```

### 3. Backup Verification
```bash
# Verify latest backup exists
ls -lh /backups/vendorvault/ | tail -5

# Test backup restore (on test environment)
mysql -u test_user -p test_db < /backups/vendorvault/latest.sql
```

## Monthly Tasks

### 1. Update Dependencies
```bash
# Check for updates
mvn versions:display-dependency-updates

# Update to latest patch versions
mvn versions:use-latest-releases

# Test after updates
mvn clean test
mvn spring-boot:run
```

### 2. Security Audit
```bash
# Check for security vulnerabilities
mvn dependency-check:check

# Review user accounts
# - Remove inactive users
# - Review admin accounts
# - Check for weak passwords

# Review audit logs
# - Unusual access patterns
# - Failed login attempts
# - Unauthorized access attempts
```

### 3. Performance Review
```sql
-- Slow queries analysis
SELECT * FROM mysql.slow_log ORDER BY query_time DESC LIMIT 10;

-- Connection pool stats
SHOW STATUS LIKE 'Threads%';
SHOW STATUS LIKE 'Connections';

-- Table optimization
OPTIMIZE TABLE contracts;
OPTIMIZE TABLE vendors;
OPTIMIZE TABLE users;
```

### 4. Data Cleanup
```sql
-- Archive old audit logs (older than 1 year)
DELETE FROM audit_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);

-- Clean up old notifications (older than 90 days)
DELETE FROM notifications WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY) AND is_read = true;

-- Archive expired contracts (older than 2 years)
-- (Move to archive table or export before deletion)
```

## Quarterly Tasks

### 1. Comprehensive Security Review
- [ ] Review all user accounts and permissions
- [ ] Update all passwords (especially service accounts)
- [ ] Review firewall rules
- [ ] Check SSL certificate expiry
- [ ] Review access logs for anomalies
- [ ] Update security patches
- [ ] Penetration testing (if applicable)

### 2. Disaster Recovery Test
- [ ] Test full database restore
- [ ] Test application deployment from backup
- [ ] Verify backup integrity
- [ ] Update disaster recovery documentation
- [ ] Test failover procedures

### 3. Capacity Planning
- [ ] Review database growth trends
- [ ] Review storage usage trends
- [ ] Review user growth
- [ ] Plan for scaling if needed
- [ ] Review and adjust resource allocation

## Annual Tasks

### 1. Major Version Updates
- [ ] Plan Spring Boot major version upgrade
- [ ] Plan Java version upgrade (if needed)
- [ ] Plan database version upgrade
- [ ] Test all updates in staging environment
- [ ] Schedule maintenance window
- [ ] Execute upgrades
- [ ] Verify all functionality

### 2. Comprehensive Audit
- [ ] Code quality review
- [ ] Security audit
- [ ] Performance audit
- [ ] Documentation review and update
- [ ] Compliance review
- [ ] User feedback review

### 3. Archive Old Data
- [ ] Archive contracts older than 5 years
- [ ] Archive vendor records (inactive > 3 years)
- [ ] Archive audit logs older than 2 years
- [ ] Export archived data to cold storage
- [ ] Verify archive integrity

## Common Maintenance Tasks

### Restart Application
```bash
# Systemd
sudo systemctl restart vendorvault

# Docker
docker-compose restart app

# Manual
pkill -f vendorvault-clm
java -jar vendorvault-clm-1.0.0.jar &
```

### Clear Cache
```bash
# Application cache (if using Redis)
redis-cli FLUSHALL

# Caffeine cache (restart application)
sudo systemctl restart vendorvault
```

### Database Backup
```bash
#!/bin/bash
# backup-database.sh

BACKUP_DIR="/backups/vendorvault"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="vendorvault_db"
DB_USER="vendorvault_user"

# Create backup
mysqldump -u $DB_USER -p $DB_NAME > $BACKUP_DIR/db_$DATE.sql

# Compress backup
gzip $BACKUP_DIR/db_$DATE.sql

# Delete backups older than 30 days
find $BACKUP_DIR -name "db_*.sql.gz" -mtime +30 -delete

echo "Backup completed: db_$DATE.sql.gz"
```

### Database Restore
```bash
#!/bin/bash
# restore-database.sh

BACKUP_FILE=$1
DB_NAME="vendorvault_db"
DB_USER="vendorvault_user"

if [ -z "$BACKUP_FILE" ]; then
    echo "Usage: ./restore-database.sh <backup_file.sql.gz>"
    exit 1
fi

# Stop application
sudo systemctl stop vendorvault

# Decompress if needed
if [[ $BACKUP_FILE == *.gz ]]; then
    gunzip -c $BACKUP_FILE > /tmp/restore.sql
    BACKUP_FILE="/tmp/restore.sql"
fi

# Restore database
mysql -u $DB_USER -p $DB_NAME < $BACKUP_FILE

# Start application
sudo systemctl start vendorvault

echo "Database restored successfully"
```

### Log Rotation
```bash
# /etc/logrotate.d/vendorvault

/opt/vendorvault/logs/*.log {
    daily
    rotate 30
    compress
    delaycompress
    notifempty
    create 0640 vendorvault vendorvault
    sharedscripts
    postrotate
        systemctl reload vendorvault > /dev/null 2>&1 || true
    endscript
}
```

### Update Application
```bash
#!/bin/bash
# update-application.sh

NEW_VERSION=$1

if [ -z "$NEW_VERSION" ]; then
    echo "Usage: ./update-application.sh <version>"
    exit 1
fi

# Backup current version
cp /opt/vendorvault/vendorvault-clm-1.0.0.jar /opt/vendorvault/vendorvault-clm-1.0.0.jar.backup

# Stop application
sudo systemctl stop vendorvault

# Deploy new version
cp vendorvault-clm-$NEW_VERSION.jar /opt/vendorvault/vendorvault-clm-1.0.0.jar

# Start application
sudo systemctl start vendorvault

# Check status
sleep 10
sudo systemctl status vendorvault

echo "Application updated to version $NEW_VERSION"
```

## Troubleshooting

### Application Won't Start
1. Check logs: `journalctl -u vendorvault -n 100`
2. Verify database is running: `systemctl status mysql`
3. Check port availability: `netstat -tulpn | grep 8080`
4. Verify environment variables: `systemctl show vendorvault | grep Environment`
5. Check disk space: `df -h`

### Database Connection Issues
1. Verify MySQL is running: `systemctl status mysql`
2. Test connection: `mysql -u vendorvault_user -p`
3. Check credentials in environment variables
4. Verify network connectivity
5. Check MySQL error log: `tail -f /var/log/mysql/error.log`

### Performance Issues
1. Check memory usage: `free -m`
2. Check CPU usage: `top`
3. Review slow queries: `SELECT * FROM mysql.slow_log`
4. Check connection pool: `SHOW STATUS LIKE 'Threads%'`
5. Review application logs for errors
6. Consider increasing JVM heap: `-Xmx2048m`

### Email Not Sending
1. Verify SMTP configuration in environment variables
2. Test SMTP connection: `telnet smtp.server.com 587`
3. Check application logs for mail errors
4. Verify credentials are correct
5. Check firewall rules for outbound SMTP

## Emergency Contacts

- **System Administrator**: [Name] - [Phone] - [Email]
- **Database Administrator**: [Name] - [Phone] - [Email]
- **Application Support**: [Name] - [Phone] - [Email]
- **Security Team**: [Email]
- **On-Call Rotation**: [Link to schedule]

## Useful Commands Reference

```bash
# View application logs
tail -f /opt/vendorvault/logs/application.log

# Check application status
systemctl status vendorvault

# Restart application
systemctl restart vendorvault

# View database size
mysql -u root -p -e "SELECT table_schema AS 'Database', ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)' FROM information_schema.TABLES GROUP BY table_schema;"

# Check active sessions
mysql -u root -p -e "SHOW PROCESSLIST;"

# Monitor system resources
htop

# Check disk I/O
iostat -x 1

# Network connections
netstat -tulpn | grep java
```

---

**Last Updated**: March 31, 2026  
**Maintained By**: Orion Heavy Industries IT Department
