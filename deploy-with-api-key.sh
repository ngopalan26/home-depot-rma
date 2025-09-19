#!/bin/bash

# Travel Packing Assistant - Deployment Script with API Key
# Run this in Google Cloud Shell

set -e

PROJECT_ID="fluent-timing-472621-r3"
REGION="us-central1"
WEATHER_API_KEY="e49958eec2334c0aee1692d1c8042ae5"
BACKEND_SERVICE="travel-backend"
FRONTEND_SERVICE="travel-frontend"

echo "🚀 Travel Packing Assistant - Deployment with API Key"
echo "Project ID: $PROJECT_ID"
echo "Region: $REGION"
echo "Weather API Key: $WEATHER_API_KEY"

# Set project
gcloud config set project $PROJECT_ID

# Enable required APIs
echo "📋 Enabling required APIs..."
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com

# Build and deploy backend
echo "🏗️  Building and deploying backend..."
cd travel-backend
gcloud builds submit --tag gcr.io/$PROJECT_ID/$BACKEND_SERVICE

echo "🚀 Deploying backend to Cloud Run..."
gcloud run deploy $BACKEND_SERVICE \
    --image gcr.io/$PROJECT_ID/$BACKEND_SERVICE \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --set-env-vars WEATHER_API_KEY=$WEATHER_API_KEY

BACKEND_URL=$(gcloud run services describe $BACKEND_SERVICE --platform managed --region $REGION --format="value(status.url)")
echo "✅ Backend deployed to: $BACKEND_URL"

# Build and deploy frontend
echo "🏗️  Building and deploying frontend..."
cd ../travel-frontend
gcloud builds submit --tag gcr.io/$PROJECT_ID/$FRONTEND_SERVICE

echo "🚀 Deploying frontend to Cloud Run..."
gcloud run deploy $FRONTEND_SERVICE \
    --image gcr.io/$PROJECT_ID/$FRONTEND_SERVICE \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --set-env-vars REACT_APP_API_BASE_URL=$BACKEND_URL/api

FRONTEND_URL=$(gcloud run services describe $FRONTEND_SERVICE --platform managed --region $REGION --format="value(status.url)")
echo "✅ Frontend deployed to: $FRONTEND_URL"

echo ""
echo "🎉 Deployment Complete!"
echo "=================================="
echo "Frontend URL: $FRONTEND_URL"
echo "Backend URL: $BACKEND_URL"
echo "=================================="
echo ""
echo "🌐 Your Travel Packing Assistant is now live and accessible worldwide!"
echo "📱 Share this URL with anyone: $FRONTEND_URL"
