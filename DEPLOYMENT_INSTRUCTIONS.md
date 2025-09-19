# 🚀 Travel Packing Assistant - Deployment Instructions

## ✅ What's Ready:
- ✅ Docker images built locally
- ✅ GCP Project ID: `fluent-timing-472621-r3`
- ✅ Artifact Registry API (enable it in the console that just opened)

## 📋 Step-by-Step Deployment:

### 1. Enable APIs (2 minutes)
1. **Enable Artifact Registry API**: Click "ENABLE" in the browser tab that just opened
2. **Enable Cloud Run API**: Go to [Cloud Run API](https://console.developers.google.com/apis/api/run.googleapis.com/overview?project=1090615745845) and click "ENABLE"
3. **Enable Cloud Build API**: Go to [Cloud Build API](https://console.developers.google.com/apis/api/cloudbuild.googleapis.com/overview?project=1090615745845) and click "ENABLE"

### 2. Deploy Backend to Cloud Run (5 minutes)

#### Option A: Using Cloud Build (Recommended)
1. Go to [Google Cloud Build](https://console.cloud.google.com/cloud-build/builds?project=fluent-timing-472621-r3)
2. Click **"CREATE BUILD"**
3. **Connect Repository**: Choose "Cloud Source Repositories" or "GitHub"
4. **Build Configuration**: Use "Cloud Build configuration file (yaml or json)"
5. **Create `cloudbuild.yaml`** in your project root:

```yaml
steps:
  # Build backend image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/fluent-timing-472621-r3/travel-backend', './travel-backend']
  
  # Push backend image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/fluent-timing-472621-r3/travel-backend']
  
  # Deploy to Cloud Run
  - name: 'gcr.io/cloud-builders/gcloud'
    args: [
      'run', 'deploy', 'travel-backend',
      '--image', 'gcr.io/fluent-timing-472621-r3/travel-backend',
      '--region', 'us-central1',
      '--allow-unauthenticated',
      '--set-env-vars', 'WEATHER_API_KEY=YOUR_CORRECT_API_KEY_HERE'
    ]
```

#### Option B: Manual Upload
1. Go to [Cloud Run](https://console.cloud.google.com/run?project=fluent-timing-472621-r3)
2. Click **"CREATE SERVICE"**
3. **Container image URL**: `gcr.io/fluent-timing-472621-r3/travel-backend`
4. **Service name**: `travel-backend`
5. **Region**: `us-central1`
6. **Authentication**: "Allow unauthenticated invocations"
7. **Environment variables**: Add `WEATHER_API_KEY` with your OpenWeatherMap API key

### 3. Deploy Frontend to Cloud Run (5 minutes)

#### Create `cloudbuild-frontend.yaml`:
```yaml
steps:
  # Build frontend image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/fluent-timing-472621-r3/travel-frontend', './travel-frontend']
  
  # Push frontend image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/fluent-timing-472621-r3/travel-frontend']
  
  # Deploy to Cloud Run
  - name: 'gcr.io/cloud-builders/gcloud'
    args: [
      'run', 'deploy', 'travel-frontend',
      '--image', 'gcr.io/fluent-timing-472621-r3/travel-frontend',
      '--region', 'us-central1',
      '--allow-unauthenticated'
    ]
```

### 4. Update Frontend Configuration
After backend is deployed, get the backend URL and update the frontend nginx.conf:

1. **Get Backend URL**: From Cloud Run console, copy the backend service URL
2. **Update nginx.conf**: Replace the proxy_pass URL with your backend URL
3. **Redeploy Frontend**: Run the frontend build again

## 🎯 Expected Results:
- **Backend URL**: `https://travel-backend-xxxxxx-uc.a.run.app`
- **Frontend URL**: `https://travel-frontend-xxxxxx-uc.a.run.app`
- **Public Application**: Accessible worldwide!

## 📝 Notes:
- Replace `YOUR_CORRECT_API_KEY_HERE` with your actual OpenWeatherMap API key
- The deployment will take 5-10 minutes total
- Both services will be publicly accessible
- Cloud Run has a generous free tier

## 🆘 Need Help?
If you encounter issues:
1. Check that all APIs are enabled
2. Verify your OpenWeatherMap API key is valid
3. Ensure billing is enabled on your GCP project
