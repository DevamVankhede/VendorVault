# VendorVault CLM - Deployment Guide

## Production Deployment Checklist

### Pre-Deployment

- [ ] All tests passing
- [ ] Code reviewed and approved
- [ ] Database backup created
- [ ] Environment variables configured
- [ ] SSL certificates obtained
- [ ] Production database created
- [ ] Mail server configured
- [ ] Monitoring tools set up
- [ ] Rollback plan prepared

### Environment Variables (Production)

```bash
# Database
export DB_URL=jdbc:mysql://prod-db-host:3306/vendorvault_db?useSSL=true
export DB_USERNAME=prod_user
export DB_PASSWORD=<strong-password>

# Mail
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=noreply@orion.com
export MAIL_PASSWORD=<app-password>

# Security
export VENDORVAULT_REMEMBER_ME_KEY=<generate-random-64-char-key>

# Application
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8080
```

## Deployment Options

### Option 1: Traditional Server (Linux)

#### 1. Prepare Server
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
```

#### 2. Create Application User
```bash
sudo useradd -m -s /bin/bash vendorvault
sudo mkdir -p /opt/vendorvault
sudo chown vendorvault:vendorvault /opt/vendorvault
```

#### 3. Build and Deploy
```bash
# Build on local machine
mvn clean package -DskipTests

# Copy to server
scp target/vendorvault-clm-1.0.0.jar vendorvault@server:/opt/vendorvault/

# SSH to server
ssh vendorvault@server
cd /opt/vendorvault
```

#### 4. Create Systemd Service
```bash
sudo nano /etc/systemd/system/vendorvault.service
```

```ini
[Unit]
Description=VendorVault CLM Application
After=syslog.target network.target

[Service]
User=vendorvault
WorkingDirectory=/opt/vendorvault
ExecStart=/usr/bin/java -jar /opt/vendorvault/vendorvault-clm-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="DB_URL=jdbc:mysql://localhost:3306/vendorvault_db"
Environment="DB_USERNAME=prod_user"
Environment="DB_PASSWORD=secure_password"

[Install]
WantedBy=multi-user.target
```

#### 5. Start Service
```bash
sudo systemctl daemon-reload
sudo systemctl enable vendorvault
sudo systemctl start vendorvault
sudo systemctl status vendorvault
```

#### 6. View Logs
```bash
sudo journalctl -u vendorvault -f
```

### Option 2: Docker Deployment

#### 1. Create Dockerfile
```dockerfile
FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="Orion Heavy Industries"

WORKDIR /app

# Copy JAR
COPY target/vendorvault-clm-1.0.0.jar app.jar

# Create uploads directory
RUN mkdir -p /app/uploads

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-Xms512m", "-Xmx2048m", "-jar", "app.jar"]
```

#### 2. Create docker-compose.yml (Production)
```yaml
version: '3.8'

services:
  app:
    build: .
    container_name: vendorvault-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:mysql://db:3306/vendorvault_db
      - DB_USERNAME=vendorvault_user
      - DB_PASSWORD=${DB_PASSWORD}
      - MAIL_HOST=${MAIL_HOST}
      - MAIL_PORT=${MAIL_PORT}
      - MAIL_USERNAME=${MAIL_USERNAME}
      - MAIL_PASSWORD=${MAIL_PASSWORD}
    depends_on:
      - db
    volumes:
      - uploads:/app/uploads
    restart: unless-stopped

  db:
    image: mysql:8.0
    container_name: vendorvault-db
    environment:
      - MYSQL_DATABASE=vendorvault_db
      - MYSQL_USER=vendorvault_user
      - MYSQL_PASSWORD=${DB_PASSWORD}
      - MYSQL_ROOT_PASSWORD=${DB_ROOT_PASSWORD}
    volumes:
      - mysql-data:/var/lib/mysql
    restart: unless-stopped

volumes:
  mysql-data:
  uploads:
```

#### 3. Deploy with Docker
```bash
# Build image
docker build -t vendorvault-clm:1.0.0 .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f app
```

### Option 3: Cloud Platform Deployment

#### AWS Elastic Beanstalk

```bash
# Install EB CLI
pip install awsebcli

# Initialize
eb init -p java-17 vendorvault-clm --region us-east-1

# Create environment
eb create vendorvault-prod --database.engine mysql --database.username admin

# Set environment variables
eb setenv SPRING_PROFILES_ACTIVE=prod \
  DB_PASSWORD=secure_password \
  MAIL_HOST=smtp.gmail.com \
  MAIL_USERNAME=noreply@orion.com \
  MAIL_PASSWORD=app_password

# Deploy
eb deploy

# Open in browser
eb open
```

#### Heroku

```bash
# Login
heroku login

# Create app
heroku create vendorvault-clm

# Add MySQL
heroku addons:create jawsdb:kitefin

# Set environment variables
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set MAIL_HOST=smtp.gmail.com
heroku config:set MAIL_USERNAME=noreply@orion.com
heroku config:set MAIL_PASSWORD=app_password

# Deploy
git push heroku main

# Open app
heroku open
```

#### Railway

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# Initialize project
railway init

# Link to project
railway link

# Deploy
railway up

# Set environment variables via Railway dashboard
```

## Nginx Reverse Proxy Setup

### 1. Install Nginx
```bash
sudo apt install nginx -y
```

### 2. Configure Nginx
```bash
sudo nano /etc/nginx/sites-available/vendorvault
```

```nginx
server {
    listen 80;
    server_name vendorvault.orion.com;

    # Redirect to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name vendorvault.orion.com;

    # SSL Configuration
    ssl_certificate /etc/ssl/certs/vendorvault.crt;
    ssl_certificate_key /etc/ssl/private/vendorvault.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # Logging
    access_log /var/log/nginx/vendorvault-access.log;
    error_log /var/log/nginx/vendorvault-error.log;

    # Proxy settings
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket support (if needed)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Static files caching
    location ~* \.(css|js|jpg|jpeg|png|gif|ico|svg|woff|woff2|ttf|eot)$ {
        proxy_pass http://localhost:8080;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 3. Enable Site
```bash
sudo ln -s /etc/nginx/sites-available/vendorvault /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## SSL Certificate Setup (Let's Encrypt)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtain certificate
sudo certbot --nginx -d vendorvault.orion.com

# Auto-renewal (already configured)
sudo certbot renew --dry-run
```

## Post-Deployment

### 1. Verify Deployment
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Check database connection
mysql -u vendorvault_user -p vendorvault_db -e "SELECT COUNT(*) FROM users;"

# Check logs
tail -f /var/log/vendorvault/application.log
```

### 2. Change Default Passwords
- Login as admin
- Navigate to Profile → Change Password
- Update admin password immediately

### 3. Configure Monitoring
- Set up application monitoring (New Relic, Datadog, etc.)
- Configure log aggregation (ELK Stack, Splunk, etc.)
- Set up uptime monitoring (Pingdom, UptimeRobot, etc.)

### 4. Set Up Backups
```bash
# Database backup script
#!/bin/bash
BACKUP_DIR="/backups/vendorvault"
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u vendorvault_user -p'password' vendorvault_db > $BACKUP_DIR/db_$DATE.sql
find $BACKUP_DIR -name "db_*.sql" -mtime +7 -delete
```

Add to crontab:
```bash
0 2 * * * /opt/scripts/backup-vendorvault.sh
```

## Rollback Procedure

### 1. Stop Application
```bash
sudo systemctl stop vendorvault
```

### 2. Restore Previous Version
```bash
cp /opt/vendorvault/vendorvault-clm-1.0.0.jar.backup /opt/vendorvault/vendorvault-clm-1.0.0.jar
```

### 3. Restore Database (if needed)
```bash
mysql -u vendorvault_user -p vendorvault_db < /backups/vendorvault/db_backup.sql
```

### 4. Start Application
```bash
sudo systemctl start vendorvault
```

## Monitoring & Alerts

### Key Metrics to Monitor
- Application uptime
- Response time
- Error rate
- Database connection pool
- Memory usage
- CPU usage
- Disk space
- Active sessions

### Recommended Tools
- **APM**: New Relic, Datadog, AppDynamics
- **Logs**: ELK Stack, Splunk, Graylog
- **Uptime**: Pingdom, UptimeRobot, StatusCake
- **Alerts**: PagerDuty, Opsgenie

## Security Hardening

- [ ] Enable firewall (UFW/iptables)
- [ ] Disable root SSH login
- [ ] Use SSH keys instead of passwords
- [ ] Keep system updated
- [ ] Use strong database passwords
- [ ] Enable database SSL
- [ ] Implement rate limiting
- [ ] Set up fail2ban
- [ ] Regular security audits
- [ ] Enable audit logging

---

**Need Help?** Contact DevOps team or refer to [README.md](README.md)
