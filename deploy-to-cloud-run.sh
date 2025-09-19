#!/bin/bash

# Travel Packing Assistant - Google Cloud Run Deployment Script
# This script deploys both backend and frontend to Google Cloud Run

set -e

# Configuration
PROJECT_ID="your-gcp-project-id"  # Replace with your actual GCP project ID
REGION="us-central1"
BACKEND_SERVICE="travel-packing-backend"
FRONTEND_SERVICE="travel-packing-frontend"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Travel Packing Assistant - Cloud Run Deployment${NC}"
echo "=================================================="

# Check if gcloud is installed
if ! command -v gcloud &> /dev/null; then
    echo -e "${RED}❌ gcloud CLI is not installed. Please install it first.${NC}"
    echo "Visit: https://cloud.google.com/sdk/docs/install"
    exit 1
fi

# Check if user is authenticated
if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | grep -q .; then
    echo -e "${YELLOW}⚠️  Not authenticated with gcloud. Please run: gcloud auth login${NC}"
    exit 1
fi

# Set project
echo -e "${BLUE}📋 Setting project to: ${PROJECT_ID}${NC}"
gcloud config set project $PROJECT_ID

# Enable required APIs
echo -e "${BLUE}🔧 Enabling required APIs...${NC}"
gcloud services enable cloudbuild.googleapis.com
gcloud services enable run.googleapis.com
gcloud services enable containerregistry.googleapis.com

# Deploy Backend
echo -e "${BLUE}🏗️  Deploying Backend...${NC}"
cd travel-backend

# Build and deploy backend
gcloud run deploy $BACKEND_SERVICE \
    --source . \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --port 8080 \
    --memory 1Gi \
    --cpu 1 \
    --max-instances 10 \
    --set-env-vars="WEATHER_API_KEY=YOUR_CORRECT_API_KEY_HERE"

# Get backend URL
BACKEND_URL=$(gcloud run services describe $BACKEND_SERVICE --platform managed --region $REGION --format 'value(status.url)')
echo -e "${GREEN}✅ Backend deployed at: ${BACKEND_URL}${NC}"

cd ..

# Deploy Frontend
echo -e "${BLUE}🏗️  Deploying Frontend...${NC}"
cd travel-frontend

# Update nginx config with backend URL
sed -i.bak "s|\${BACKEND_URL}|${BACKEND_URL}|g" nginx.conf

# Build and deploy frontend
gcloud run deploy $FRONTEND_SERVICE \
    --source . \
    --platform managed \
    --region $REGION \
    --allow-unauthenticated \
    --port 8080 \
    --memory 512Mi \
    --cpu 1 \
    --max-instances 10

# Restore original nginx config
mv nginx.conf.bak nginx.conf

# Get frontend URL
FRONTEND_URL=$(gcloud run services describe $FRONTEND_SERVICE --platform managed --region $REGION --format 'value(status.url)')
echo -e "${GREEN}✅ Frontend deployed at: ${FRONTEND_URL}${NC}"

cd ..

echo ""
echo -e "${GREEN}🎉 Deployment Complete!${NC}"
echo "=================================================="
echo -e "${GREEN}🌐 Frontend URL: ${FRONTEND_URL}${NC}"
echo -e "${GREEN}🔧 Backend URL: ${BACKEND_URL}${NC}"
echo ""
echo -e "${YELLOW}📝 Next Steps:${NC}"
echo "1. Update your OpenWeatherMap API key in the backend environment variables"
echo "2. Test the application at the frontend URL"
echo "3. Share the frontend URL with your husband for testing"
echo ""
echo -e "${BLUE}💡 To update environment variables:${NC}"
echo "gcloud run services update $BACKEND_SERVICE --platform managed --region $REGION --set-env-vars='WEATHER_API_KEY=your-actual-api-key'"
