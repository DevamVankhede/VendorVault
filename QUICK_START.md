# VendorVault CLM - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Prerequisites Check
- ✅ Java 17+ installed
- ✅ Maven 3.9+ installed (or use included wrapper)
- ✅ MySQL 8.0 running (or Docker)

### Step 2: Setup Database

**Option A - Using Docker (Easiest):**
```bash
docker-compose up -d
```

**Option B - Manual MySQL:**
```sql
CREATE DATABASE vendorvault_db;
CREATE USER 'vendorvault_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON vendorvault_db.* TO 'vendorvault_user'@'localhost';
```

### Step 3: Configure Environment

Copy and edit `.env.example` to `.env`:
```bash
copy .env.example .env
```

Minimum required settings:
```properties
DB_PASSWORD=your_password
MAIL_USERNAME=your_mailtrap_user
MAIL_PASSWORD=your_mailtrap_pass
```

### Step 4: Run Application

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Step 5: Login

Open browser: `http://localhost:8080`

**Default Credentials:**
- Email: `admin@orion.com`
- Password: `admin123`

## 📊 What You Get Out of the Box

After first run, the system automatically creates:
- ✅ 6 System Roles
- ✅ 1 Admin User + 45 Test Users
- ✅ 15 Sample Vendors
- ✅ 35 Sample Contracts (5 expiring soon)
- ✅ 5 Approval Tasks
- ✅ 5 Performance Evaluations

## 🎯 Common Tasks

### Create a New Vendor
1. Navigate to **Vendors** → **Add New Vendor**
2. Fill in vendor details (name, PAN, GSTIN, contact info)
3. Upload documents (PAN card, GST certificate)
4. Add bank details
5. Click **Save**

### Create a New Contract
1. Navigate to **Contracts** → **Create Contract**
2. Select vendor from dropdown
3. Fill contract details (title, type, dates, value)
4. Upload contract document
5. Click **Save as Draft** or **Submit for Approval**

### Approve a Contract
1. Navigate to **Approvals** → **My Queue**
2. Click on pending approval
3. Review contract details
4. Click **Approve** or **Reject** with comments

### View Dashboard
1. Navigate to **Dashboard**
2. View KPIs: Total Contracts, Active Contracts, Expiring Soon, Pending Approvals
3. View charts and recent activities

## 🔧 Troubleshooting

**Can't connect to database?**
- Check MySQL is running: `docker ps` or `mysql -u root -p`
- Verify credentials in `.env` file

**Application won't start?**
- Check Java version: `java -version` (must be 17+)
- Check port 8080 is free: `netstat -ano | findstr :8080`

**Login fails?**
- Use default credentials: admin@orion.com / admin123
- Check database has users: `SELECT * FROM users;`

## 📚 Next Steps

- Read full [README.md](README.md) for detailed documentation
- Change default admin password
- Configure production mail server
- Set up SSL/TLS for production
- Review security settings

## 🆘 Need Help?

- Check [README.md](README.md) for detailed documentation
- Review logs in console output
- Contact: support@orion.com
