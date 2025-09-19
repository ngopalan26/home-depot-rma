#!/bin/bash

# Travel Packing Assistant - Cloud Build Deployment Script
# This script uploads source code and triggers Cloud Build

set -e

PROJECT_ID="fluent-timing-472621-r3"
REGION="us-central1"

echo "🚀 Deploying Travel Packing Assistant to Cloud Run"
echo "Project ID: $PROJECT_ID"
echo "Region: $REGION"

# Create a temporary directory for the build
TEMP_DIR=$(mktemp -d)
echo "📁 Using temp directory: $TEMP_DIR"

# Copy backend files
echo "📦 Copying backend files..."
cp -r travel-backend/* "$TEMP_DIR/"
cp cloudbuild-backend.yaml "$TEMP_DIR/cloudbuild.yaml"

# Upload and build backend
echo "🏗️  Building backend..."
cd "$TEMP_DIR"
gcloud builds submit --config cloudbuild.yaml --substitutions=_PROJECT_ID=$PROJECT_ID,_REGION=$REGION

# Clean up and prepare frontend
cd /Users/nirmalagopalan/home-depot-rma
rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"

# Copy frontend files
echo "📦 Copying frontend files..."
cp -r travel-frontend/* "$TEMP_DIR/"
cp cloudbuild-frontend.yaml "$TEMP_DIR/cloudbuild.yaml"

# Upload and build frontend
echo "🏗️  Building frontend..."
cd "$TEMP_DIR"
gcloud builds submit --config cloudbuild.yaml --substitutions=_PROJECT_ID=$PROJECT_ID,_REGION=$REGION

echo "✅ Deployment complete!"
echo "🎉 Your Travel Packing Assistant is now live on Google Cloud Run!"
