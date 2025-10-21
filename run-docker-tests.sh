#!/bin/bash

# Mobile Automation Framework - Docker Test Runner
# This script builds and runs tests in Docker containers

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Mobile Automation Framework - Docker Execution ===${NC}"
echo ""

# Parse arguments
PLATFORM="${1:-android}"
BUILD="${2:-false}"

if [ "$BUILD" = "build" ] || [ "$BUILD" = "--build" ]; then
    echo -e "${YELLOW}Building Docker images...${NC}"
    docker-compose build
    echo -e "${GREEN}✓ Docker images built successfully${NC}"
    echo ""
fi

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}✗ Docker is not running. Please start Docker and try again.${NC}"
    exit 1
fi

# Run tests (Appium is started automatically within the container)
echo -e "${YELLOW}Running tests on platform: ${PLATFORM}${NC}"
echo ""

docker-compose run --rm \
    -e PLATFORM=${PLATFORM} \
    mobile-tests

TEST_EXIT_CODE=$?

# Cleanup
echo ""
echo -e "${YELLOW}Stopping Appium server...${NC}"
docker-compose down

if [ $TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Tests completed successfully${NC}"
else
    echo -e "${RED}✗ Tests failed with exit code: ${TEST_EXIT_CODE}${NC}"
fi

echo ""
echo -e "${GREEN}Reports available at: ./reports/${NC}"

exit $TEST_EXIT_CODE
