# Dockerfile for Mobile Automation Framework with Appium
# Base: Maven + Java 11 (pre-configured and optimized)

FROM maven:3.9.5-eclipse-temurin-11

LABEL maintainer="mobile-automation-team"
LABEL description="Mobile Automation Framework - Local Execution with Appium"

# Install Node.js and Appium only (Java + Maven already included in base image)
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    gnupg \
    usbutils \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y --no-install-recommends nodejs \
    && npm install -g npm@latest \
    && npm install -g appium@2.2.1 \
    && appium driver install uiautomator2 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Create working directory
WORKDIR /mobile-automation-framework

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:resolve dependency:resolve-plugins -B

# Copy rest of the project
COPY . .

# Expose Appium port
EXPOSE 4723

# Create startup script with better error handling
RUN printf '#!/bin/bash\n\
set -e\n\
\n\
echo "=== Mobile Automation Framework ==="\n\
echo "Platform: ${PLATFORM:-android}"\n\
echo "Execution Mode: ${EXECUTION_MODE:-local}"\n\
\n\
# Start Appium\n\
echo "Starting Appium server..."\n\
appium --address 0.0.0.0 --port 4723 --allow-insecure=adb_shell > /tmp/appium.log 2>&1 &\n\
APPIUM_PID=$!\n\
\n\
# Wait for Appium to be ready\n\
echo "Waiting for Appium to start..."\n\
for i in {1..30}; do\n\
    if curl -s http://localhost:4723/status > /dev/null 2>&1; then\n\
        echo "Appium is ready!"\n\
        break\n\
    fi\n\
    if [ $i -eq 30 ]; then\n\
        echo "Appium failed to start. Logs:"\n\
        cat /tmp/appium.log\n\
        exit 1\n\
    fi\n\
    sleep 1\n\
done\n\
\n\
# Run tests\n\
echo "Running tests..."\n\
mvn clean test \\\n\
    -Dexecution.mode=${EXECUTION_MODE:-local} \\\n\
    -Dplatform=${PLATFORM:-android} \\\n\
    || TEST_FAILED=true\n\
\n\
# Cleanup\n\
echo "Stopping Appium..."\n\
kill $APPIUM_PID 2>/dev/null || true\n\
\n\
# Exit with test result\n\
if [ "$TEST_FAILED" = true ]; then\n\
    echo "Tests failed!"\n\
    exit 1\n\
else\n\
    echo "Tests passed!"\n\
    exit 0\n\
fi\n\
' > /usr/local/bin/run-tests.sh && chmod +x /usr/local/bin/run-tests.sh

# Default command
CMD ["/usr/local/bin/run-tests.sh"]
