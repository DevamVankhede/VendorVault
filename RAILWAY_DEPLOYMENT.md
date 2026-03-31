# Deploy VendorVault CLM to Railway

## Quick Deployment Guide

### Step 1: Prerequisites

1. **Create a Railway account**: https://railway.app/
2. **Install Railway CLI** (Optional but recommended):
   ```bash
   npm install -g @railway/cli
   ```
3. **Have Git installed** and your project in a Git repository

### Step 2: Push to GitHub (if not already)

```bash
# Initialize git (if not done)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - VendorVault CLM"

# Create a new repository on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/vendorvault-clm.git
git branch -M main
git push -u origin main
```

### Step 3: Deploy to Railway

#### Option A: Using Railway Dashboard (Easiest)

1. **Go to Railway**: https://railway.app/
2. **Click "New Project"**
3. **Select "Deploy from GitHub repo"**
4. **Connect your GitHub account** (if not connected)
5. **Select your `vendorvault-clm` repository**
6. Railway will automatically detect it's a Java/Maven project

#### Option B: Using Railway CLI

```bash
# Login to Railway
railway login

# Initialize project
railway init

# Link to your project
railway link

# Deploy
railway up
```

### Step 4: Add MySQL Database

1. In your Railway project dashboard, click **"+ New"**
2. Select **"Database"** → **"Add MySQL"**
3. Railway will automatically create a MySQL database
4. Railway will automatically set `DATABASE_URL` environment variable
5. **IMPORTANT**: The app will automatically detect and convert Railway's `DATABASE_URL` format

### Step 5: Configure Environment Variables

In Railway dashboard, go to your **web service** → **Variables** tab and add:

```bash
# Spring Profile (REQUIRED)
SPRING_PROFILES_ACTIVE=prod

# Database - Railway provides DATABASE_URL automatically
# The app will auto-clway MySQL service>

# Or use the DATABASE_URL that Railway provides
SPRING_DATASOURCE_URL=${DATABASE_URL}

# Mail Configuration (use your actual SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Security
VENDORVAULT_REMEMBER_ME_KEY=generate-a-random-secure-key-here

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Java Options (optional)
JAVA_OPTS=-Xmx512m -Xms256m
```

### Step 6: Configure Database Connection for Railway

Railway provides a `DATABASE_URL` in the format:
```
mysql://user:password@host:port/database
```

Update your `application-prod.properties` to use this:

```properties
# Use Railway's DATABASE_URL if available
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/vendorvault_db}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

### Step 7: Deploy!

Railway will automatically:
1. ✅ Detect Java 17
2. ✅ Install Maven
3. ✅ Run `mvn clean package -DskipTests`
4. ✅ Start your application
5. ✅ Assign a public URL

### Step 8: Access Your Application

1. Go to your Railway project dashboard
2. Click on your service
3. Go to **"Settings"** → **"Networking"**
4. Click **"Generate Domain"**
5. Your app will be available at: `https://your-app-name.up.railway.app`

## Important Configuration

### Update application-prod.properties

Make sure your production config can handle Railway's environment:

```properties
# Server port (Railway assigns dynamically)
server.port=${PORT:8080}

# Database (Railway provides DATABASE_URL)
spring.datasource.url=${SPRING_DATASOURCE_URL:${DATABASE_URL:jdbc:mysql://localhost:3306/vendorvault_db}}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Logging
logging.level.com.orion.vendorvault=INFO
```

## Troubleshooting

### Build Fails

**Issue**: Maven build fails
**Solution**: Check Railway build logs. Common issues:
- Java version mismatch (ensure Java 17)
- Missing dependencies
- Out of memory (increase Railway plan)

### Database Connection Fails

**Issue**: Can't connect to MySQL
**Solution**:
1. Verify MySQL service is running in Railway
2. Check environment variables are set correctly
3. Ensure `DATABASE_URL` is being used
4. Check Railway MySQL service is in the same project

### Application Crashes on Startup

**Issue**: App starts but crashes
**Solution**:
1. Check Railway logs: `railway logs`
2. Verify all environment variables are set
3. Check Flyway migrations are running
4. Ensure database is accessible

### Port Binding Error

**Issue**: "Port already in use"
**Solution**: Railway assigns port dynamically via `$PORT` environment variable. Ensure your app uses it:
```java
server.port=${PORT:8080}
```

## Railway CLI Commands

```bash
# View logs
railway logs

# Open dashboard
railway open

# Check status
railway status

# Set environment variable
railway variables set KEY=value

# Connect to database
railway connect mysql

# Restart service
railway restart
```

## Cost Estimation

Railway offers:
- **Free Trial**: $5 credit (no credit card required)
- **Hobby Plan**: $5/month (includes $5 credit)
- **Pro Plan**: $20/month (includes $20 credit)

Typical usage for this app:
- **App Service**: ~$3-5/month
- **MySQL Database**: ~$2-3/month
- **Total**: ~$5-8/month

## Post-Deployment Checklist

- [ ] Application is accessible via Railway URL
- [ ] Database migrations ran successfully
- [ ] Can login with admin credentials
- [ ] All features working (vendors, contracts, approvals)
- [ ] Email notifications configured (if using real SMTP)
- [ ] Changed default admin password
- [ ] Set up custom domain (optional)
- [ ] Configure SSL (automatic with Railway)
- [ ] Set up monitoring/alerts
- [ ] Configure backups for database

## Custom Domain (Optional)

1. Go to Railway dashboard → Your service → **Settings**
2. Scroll to **Networking** → **Custom Domains**
3. Click **Add Custom Domain**
4. Enter your domain (e.g., `vendorvault.yourdomain.com`)
5. Add the CNAME record to your DNS provider:
   ```
   CNAME: vendorvault → your-app.up.railway.app
   ```
6. Wait for DNS propagation (5-30 minutes)
7. Railway automatically provisions SSL certificate

## Monitoring

Railway provides:
- **Metrics**: CPU, Memory, Network usage
- **Logs**: Real-time application logs
- **Alerts**: Set up notifications for crashes

Access via: Railway Dashboard → Your Service → **Observability**

## Backup Strategy

### Database Backups

Railway doesn't provide automatic backups on free tier. Options:

1. **Manual Backup**:
   ```bash
   railway connect mysql
   mysqldump -u user -p database > backup.sql
   ```

2. **Automated Backup Script** (run via cron or GitHub Actions):
   ```bash
   railway run mysqldump database > backup_$(date +%Y%m%d).sql
   ```

3. **Upgrade to Pro Plan**: Includes automated backups

## Scaling

To handle more traffic:

1. **Vertical Scaling**: Upgrade Railway plan for more resources
2. **Horizontal Scaling**: Railway Pro supports multiple replicas
3. **Database Optimization**: Add indexes, optimize queries
4. **Caching**: Already configured with Caffeine

## Support

- **Railway Docs**: https://docs.railway.app/
- **Railway Discord**: https://discord.gg/railway
- **Railway Status**: https://status.railway.app/

---

**Need Help?** Check the main [README.md](README.md) or [DEPLOYMENT.md](DEPLOYMENT.md) for more details.
