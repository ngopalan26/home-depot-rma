#!/bin/sh

# Get backend URL from environment variable
BACKEND_URL=${REACT_APP_API_BASE_URL:-"http://localhost:8081"}

# Remove /api suffix if present
BACKEND_URL=$(echo $BACKEND_URL | sed 's|/api$||')

echo "Configuring nginx with backend URL: $BACKEND_URL"

# Create nginx config with the actual backend URL
envsubst '${BACKEND_URL}' < /etc/nginx/nginx.template > /etc/nginx/nginx.conf

# Start nginx
nginx -g "daemon off;"
