#!/bin/bash

# 🚀 ONE-CLICK DEPLOYMENT SCRIPT
# Travel Packing Assistant - Google Cloud Run
# Just copy and paste this entire script into Cloud Shell!

set -e

# Configuration
PROJECT_ID="fluent-timing-472621-r3"
REGION="us-central1"
WEATHER_API_KEY="e49958eec2334c0aee1692d1c8042ae5"

echo "🚀 TRAVEL PACKING ASSISTANT - ONE-CLICK DEPLOYMENT"
echo "=================================================="
echo "Project: $PROJECT_ID"
echo "Region: $REGION"
echo "Weather API Key: $WEATHER_API_KEY"
echo ""

# Step 1: Clone repository
echo "📥 Step 1: Cloning repository..."
git clone https://github.com/ngopalan26/travel-packing-assistant.git
cd travel-packing-assistant
echo "✅ Repository cloned successfully!"
echo ""

# Step 2: Set up Google Cloud
echo "⚙️ Step 2: Setting up Google Cloud..."
gcloud config set project $PROJECT_ID
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com
echo "✅ Google Cloud setup complete!"
echo ""

# Step 3: Deploy Backend
echo "��️ Step 3: Deploying backend..."
cd travel-backend
echo "Building backend Docker image..."
gcloud builds submit --tag gcr.io/$PROJECT_ID/travel-backend

echo "Creating Cloud Run service for backend..."
gcloud run deploy travel-backend \
    --image gcr.io/$PROJECT_ID/travel-backend \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --set-env-vars WEATHER_API_KEY=$WEATHER_API_KEY \
    --memory 1Gi \
    --cpu 1 \
    --timeout 300

BACKEND_URL=$(gcloud run services describe travel-backend --platform managed --region $REGION --format="value(status.url)")
echo "✅ Backend deployed successfully!"
echo "Backend URL: $BACKEND_URL"
echo ""

# Step 4: Deploy Frontend
echo "📱 Step 4: Deploying frontend..."
cd ../travel-frontend
echo "Building frontend Docker image..."
gcloud builds submit --tag gcr.io/$PROJECT_ID/travel-frontend

echo "Creating Cloud Run service for frontend..."
gcloud run deploy travel-frontend \
    --image gcr.io/$PROJECT_ID/travel-frontend \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --set-env-vars REACT_APP_API_BASE_URL=$BACKEND_URL/api \
    --memory 512Mi \
    --cpu 1 \
    --timeout 300

FRONTEND_URL=$(gcloud run services describe travel-frontend --platform managed --region $REGION --format="value(status.url)")
echo "✅ Frontend deployed successfully!"
echo "Frontend URL: $FRONTEND_URL"
echo ""

# Step 5: Final Results
echo "🎉 DEPLOYMENT COMPLETE!"
echo "=================================="
echo "🌐 Your Travel Packing Assistant is now LIVE!"
echo ""
echo "📱 Frontend URL: $FRONTEND_URL"
echo "🔧 Backend URL:  $BACKEND_URL"
echo ""
echo "✨ Share this URL with anyone: $FRONTEND_URL"
echo ""
echo "🧪 Test your app:"
echo "1. Open the frontend URL"
echo "2. Enter travel details (New York, tomorrow)"
echo "3. Click 'Get Packing Instructions'"
echo "4. See weather data and packing suggestions!"
echo ""
echo "🚀 Your app is now accessible worldwide!"
echo "=================================="
