# 🚀 Quick Deploy to Railway

## 5-Minute Deployment

### 1️⃣ Push to GitHub

```bash
# If not already in git
git init
git add .
git commit -m "Ready for Railway deployment"

# Create repo on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/vendorvault-clm.git
git branch -M main
git push -u origin main
```

### 2️⃣ Deploy on Railway

1. Go to **https://railway.app/**
2. Click **"Start a New Project"**
3. Select **"Deploy from GitHub repo"**
4. Choose **`vendorvault-clm`**
5. Railway will auto-detect and build!

### 3️⃣ Add MySQL Database

1. In your project, click **"+ New"**
2. Select **"Database"** → **"MySQL"**
3. Done! Railway auto-connects it

### 4️⃣ Set Environment Variables

Click your app service → **Variables** → Add these:

```bash
SPRING_PROFILES_ACTIVE=prod
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
VENDORVAULT_REMEMBER_ME_KEY=your-random-secure-key-here
```

**Note**: Railway automatically provides `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` from MySQL service!

### 5️⃣ Generate Public URL

1. Go to **Settings** → **Networking**
2. Click **"Generate Domain"**
3. Your app is live at: `https://your-app.up.railway.app`

## ✅ That's It!

Your VendorVault CLM is now live on Railway!

### Default Login:
- **URL**: `https://your-app.up.railway.app`
- **Email**: admin@orion.com
- **Password**: admin123

**⚠️ Change the password immediately after first login!**

## 💰 Cost

- **Free Trial**: $5 credit (no card needed)
- **Estimated Monthly**: $5-8
  - App: ~$3-5
  - MySQL: ~$2-3

## 📊 Monitor Your App

- **Logs**: Railway Dashboard → Your Service → **Logs**
- **Metrics**: Railway Dashboard → Your Service → **Metrics**
- **Database**: Railway Dashboard → MySQL Service → **Data**

## 🔧 Useful Commands

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# View logs
railway logs

# Open dashboard
railway open

# Restart app
railway restart
```

## 🆘 Troubleshooting

**Build fails?**
- Check Java 17 is being used
- View build logs in Railway dashboard

**Can't connect to database?**
- Ensure MySQL service is running
- Check environment variables are set

**App crashes?**
- View logs: `railway logs`
- Check all required env vars are set

## 📚 Full Documentation

See [RAILWAY_DEPLOYMENT.md](RAILWAY_DEPLOYMENT.md) for complete guide.

---

**Questions?** Check [README.md](README.md) or Railway docs: https://docs.railway.app/
