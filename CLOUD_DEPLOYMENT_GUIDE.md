# 🚀 Travel Packing Assistant - Cloud Run Deployment Guide

This guide will help you deploy your Travel Packing Assistant to Google Cloud Run for a permanent, publicly accessible URL.

## 📋 Prerequisites

1. **Google Cloud Account**: Sign up at [Google Cloud Console](https://console.cloud.google.com/)
2. **Billing Enabled**: Cloud Run requires billing to be enabled
3. **OpenWeatherMap API Key**: Get your free API key from [OpenWeatherMap](https://openweathermap.org/api)

## 🛠️ Setup Steps

### 1. Install Google Cloud CLI

**On macOS:**
```bash
# Download and install
curl https://sdk.cloud.google.com | bash
exec -l $SHELL

# Or using Homebrew
brew install google-cloud-sdk
```

**On Windows:**
- Download from [Google Cloud SDK](https://cloud.google.com/sdk/docs/install-sdk#windows)

**On Linux:**
```bash
curl https://sdk.cloud.google.com | bash
exec -l $SHELL
```

### 2. Authenticate with Google Cloud

```bash
gcloud auth login
gcloud auth application-default login
```

### 3. Create a Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Click "Select a project" → "New Project"
3. Enter project name: `travel-packing-assistant`
4. Note your **Project ID** (you'll need this)

### 4. Update Deployment Script

Edit `deploy-to-cloud-run.sh` and replace:
```bash
PROJECT_ID="your-gcp-project-id"  # Replace with your actual project ID
```

With your actual project ID:
```bash
PROJECT_ID="travel-packing-assistant-12345"  # Your actual project ID
```

### 5. Run Deployment

```bash
./deploy-to-cloud-run.sh
```

This will:
- ✅ Enable required Google Cloud APIs
- ✅ Build and deploy the backend to Cloud Run
- ✅ Build and deploy the frontend to Cloud Run
- ✅ Configure the frontend to connect to the backend
- ✅ Provide you with the public URLs

## 🔧 Post-Deployment Configuration

### Update Weather API Key

After deployment, update your OpenWeatherMap API key:

```bash
# Replace with your actual API key
gcloud run services update travel-packing-backend \
    --platform managed \
    --region us-central1 \
    --set-env-vars='WEATHER_API_KEY=your-actual-openweathermap-api-key'
```

## 🌐 Access Your Application

After deployment, you'll get two URLs:
- **Frontend URL**: Your public application URL (share this with your husband)
- **Backend URL**: Internal API URL (for debugging)

## 💰 Cost Estimation

Google Cloud Run pricing (approximate):
- **Free Tier**: 2 million requests per month
- **After Free Tier**: ~$0.40 per million requests
- **CPU/Memory**: ~$0.00002400 per vCPU-second, ~$0.00000250 per GB-second

For a personal project, you'll likely stay within the free tier.

## 🔍 Troubleshooting

### Common Issues:

1. **"Project not found"**
   - Verify your project ID in the deployment script
   - Ensure billing is enabled

2. **"API not enabled"**
   - The script automatically enables required APIs
   - Wait a few minutes and retry

3. **"Permission denied"**
   - Run `gcloud auth login` again
   - Ensure you have the necessary permissions

4. **"Build failed"**
   - Check that all files are in the correct directories
   - Verify Docker files are properly formatted

### View Logs:

```bash
# Backend logs
gcloud run services logs read travel-packing-backend --region us-central1

# Frontend logs
gcloud run services logs read travel-packing-frontend --region us-central1
```

## 🎯 Benefits of Cloud Run Deployment

✅ **Permanent URL**: No more ngrok limitations
✅ **Full Functionality**: API calls work perfectly
✅ **Scalable**: Automatically handles traffic spikes
✅ **Cost-Effective**: Pay only for what you use
✅ **Professional**: Production-ready deployment
✅ **Easy Updates**: Simple redeployment process

## 🔄 Updating Your Application

To update your application:
1. Make changes to your code
2. Run `./deploy-to-cloud-run.sh` again
3. Cloud Run will automatically update the services

## 📞 Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Review the Google Cloud Run documentation
3. Check the deployment logs using the commands above

---

**🎉 Congratulations!** Your Travel Packing Assistant will be live on the internet with full functionality!
