#!/bin/bash

# Travel Packing Assistant - Cloud Shell Deployment Script
# Run this in Google Cloud Shell

set -e

PROJECT_ID="fluent-timing-472621-r3"
REGION="us-central1"
BACKEND_SERVICE="travel-backend"
FRONTEND_SERVICE="travel-frontend"

echo "🚀 Travel Packing Assistant - Cloud Shell Deployment"
echo "Project ID: $PROJECT_ID"
echo "Region: $REGION"

# Enable required APIs
echo "📋 Enabling required APIs..."
gcloud services enable run.googleapis.com cloudbuild.googleapis.com artifactregistry.googleapis.com

# Create a temporary directory
TEMP_DIR=$(mktemp -d)
echo "📁 Using temp directory: $TEMP_DIR"

# Download source code (you'll need to upload your files to Cloud Shell)
echo "📦 Please upload your travel-backend and travel-frontend folders to Cloud Shell"
echo "Then run this script again"

# Build and deploy backend
echo "🏗️  Building backend..."
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
echo "🏗️  Building frontend..."
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

echo "🎉 Deployment Complete!"
echo "Frontend URL: $FRONTEND_URL"
echo "Backend URL: $BACKEND_URL"
