#!/bin/bash
# Builds the React app in frontend/ and copies the output into the
# backend's static resources, so "mvn spring-boot:run" (run from backend/)
# afterwards serves both the API and the UI from the same port.
# Run this again any time you change frontend code.
#
# Usage (from the project root):  ./build-frontend.sh

set -e

echo "==> Installing frontend dependencies..."
cd frontend
npm install

echo "==> Building the React app..."
npm run build

echo "==> Copying build output into backend/src/main/resources/static..."
rm -rf ../backend/src/main/resources/static/*
cp -r dist/* ../backend/src/main/resources/static/

cd ..
echo "==> Done. Now run:  cd backend && mvn spring-boot:run"
