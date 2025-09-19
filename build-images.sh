#!/bin/bash

# Travel Packing Assistant - Docker Image Builder
# This script builds Docker images for both backend and frontend

set -e

# Configuration - UPDATE THESE VALUES
PROJECT_ID="fluent-timing-472621-r3"  # Your actual GCP project ID

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 Building Docker Images for Travel Packing Assistant${NC}"
echo "================================================================"

# Check if PROJECT_ID is set
if [ "$PROJECT_ID" = "your-gcp-project-id" ]; then
    echo -e "${RED}❌ Please update PROJECT_ID in this script first!${NC}"
    echo "Edit build-images.sh and replace 'your-gcp-project-id' with your actual GCP project ID"
    exit 1
fi

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running. Please start Docker Desktop first.${NC}"
    exit 1
fi

echo -e "${YELLOW}📋 Building Backend Image...${NC}"
cd travel-backend
docker build -t gcr.io/$PROJECT_ID/travel-backend .
echo -e "${GREEN}✅ Backend image built successfully!${NC}"

echo -e "${YELLOW}📋 Building Frontend Image...${NC}"
cd ../travel-frontend
docker build -t gcr.io/$PROJECT_ID/travel-frontend .
echo -e "${GREEN}✅ Frontend image built successfully!${NC}"

echo -e "${BLUE}🎉 All images built successfully!${NC}"
echo ""
echo -e "${YELLOW}📝 Next Steps:${NC}"
echo "1. Push images to Google Container Registry:"
echo "   docker push gcr.io/$PROJECT_ID/travel-backend"
echo "   docker push gcr.io/$PROJECT_ID/travel-frontend"
echo ""
echo "2. Deploy via Google Cloud Console:"
echo "   https://console.cloud.google.com/run"
echo ""
echo -e "${GREEN}🚀 Your images are ready for deployment!${NC}"
