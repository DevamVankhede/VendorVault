# 🚂 Railway Deployment - Quick Reference Card

## 📋 Pre-Deployment Checklist

- [ ] Code pushed to GitHub
- [ ] Railway account created (https://railway.app)
- [ ] Ready to deploy!

## 🚀 Deployment Steps

```
1. Railway.app → "New Project" → "Deploy from GitHub"
2. Select repository: vendorvault-clm
3. Add MySQL: "+ New" → "Database" → "MySQL"
4. Set variables (see below)
5. Generate domain: Settings → Networking → "Generate Domain"
```

## 🔧 Required Environment Variables

```bash
SPRING_PROFILES_ACTIVE=prod
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
VENDORVAULT_REMEMBER_ME_KEY=random-64-char-key
```

**Note**: `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` are auto-provided by Railway MySQL!

## 🔑 Default Login

```
URL: https://your-app.up.railway.app
Email: admin@orion.com
Password: admin123
```

**⚠️ CHANGE PASSWORD IMMEDIATELY!**

## 📊 Railway CLI Commands

```bash
# Install
npm install -g @railway/cli

# Login
railway login

# View logs
railway logs

# Open dashboard
railway open

# Restart
railway restart

# Set variable
railway variables set KEY=value
```

## 💰 Pricing

- **Free Trial**: $5 credit
- **Monthly**: ~$5-8
  - App: $3-5
  - MySQL: $2-3

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails | Check logs, verify Java 17 |
| DB connection fails | Verify MySQL service running |
| App crashes | Run `railway logs` |
| Port error | Railway uses `$PORT` automatically |

## 📚 Full Guides

- **Quick Deploy**: [DEPLOY_TO_RAILWAY.md](DEPLOY_TO_RAILWAY.md)
- **Complete Guide**: [RAILWAY_DEPLOYMENT.md](RAILWAY_DEPLOYMENT.md)
- **Project Docs**: [README.md](README.md)

## ✅ Post-Deployment

1. Change admin password
2. Test all features
3. Configure real SMTP
4. Set up backups
5. Monitor logs

---

**Deploy Time**: ~5 minutes  
**Status**: 🟢 Ready  
**Support**: https://discord.gg/railway
