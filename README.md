# Mobile Automation Framework

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/yourusername/mobile-automation-framework)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Appium](https://img.shields.io/badge/Appium-2.0+-purple.svg)](https://appium.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)

A professional, production-ready mobile automation framework built with **Appium**, **Java**, **TestNG**, and **Maven**. This framework demonstrates industry best practices, design patterns, and enterprise-grade test automation for both Android and iOS platforms.

---

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [BrowserStack Integration](#browserstack-integration)
- [Test Reporting](#test-reporting)
- [CI/CD Integration](#cicd-integration)
- [Best Practices](#best-practices)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Page Object Model (POM)** architecture for maintainable test code
- **Data-Driven Testing** using JSON test data files
- **Parallel Test Execution** for faster feedback
- **Cross-Platform Support** for both Android and iOS
- **Cloud Platform Support** - BrowserStack integration for cloud-based testing
- **ExtentReports** for comprehensive HTML test reports
- **Screenshot Capture** on test failures
- **Log4j2** for detailed logging
- **TestNG** for test management and execution
- **CI/CD Ready** with Azure DevOps pipeline configuration
- **Thread-Safe** driver management for parallel execution
- **Runtime Configuration** - Override properties via command line without editing files
- **Reusable Utilities** for common mobile actions
- **Smart Wait Mechanisms** (implicit, explicit, fluent waits)
- **Mobile Gestures** (tap, swipe, scroll, long press)
- **Comprehensive Test Examples** demonstrating various scenarios

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Maven | 3.8+ | Build & Dependency Management |
| Appium | 9.3.0 | Mobile Automation Tool |
| Selenium | 4.25.0 | WebDriver Support |
| TestNG | 7.10.2 | Test Framework |
| ExtentReports | 5.1.2 | Test Reporting |
| Log4j2 | 2.24.1 | Logging Framework |
| JSON Simple | 1.1.1 | Test Data Management |

---

## Project Structure

```
mobile-automation-framework/
│
├── src/
│   ├── main/
│   │   ├── java/com/mobile/framework/
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java          # Configuration reader
│   │   │   ├── listeners/
│   │   │   │   └── TestListener.java          # TestNG listeners
│   │   │   ├── pages/
│   │   │   │   ├── BasePage.java              # Base page with common actions
│   │   │   │   ├── LoginPage.java             # Login page object
│   │   │   │   ├── HomePage.java              # Home page object
│   │   │   │   └── FormPage.java              # Form page object
│   │   │   └── utils/
│   │   │       ├── DriverManager.java         # Driver initialization & management
│   │   │       ├── ScreenshotUtil.java        # Screenshot utility
│   │   │       └── TestDataProvider.java      # Data provider for tests
│   │   └── resources/
│   │       ├── config.properties              # Framework configuration
│   │       └── log4j2.xml                     # Logging configuration
│   │
│   └── test/
│       ├── java/com/mobile/tests/
│       │   ├── BaseTest.java                  # Base test class
│       │   ├── LoginTest.java                 # Login test cases
│       │   ├── NavigationTest.java            # Navigation test cases
│       │   └── FormTest.java                  # Form test cases
│       └── resources/
│           ├── testdata/
│           │   ├── loginData.json             # Login test data
│           │   └── formData.json              # Form test data
│           └── apps/                          # Mobile apps (APK/IPA)
│
├── reports/                                   # Test reports (auto-generated)
├── logs/                                      # Test logs (auto-generated)
├── pom.xml                                    # Maven configuration
├── testng.xml                                 # TestNG suite configuration
├── azure-pipelines.yml                        # Azure DevOps CI/CD pipeline
├── .gitignore                                 # Git ignore rules
├── README.md                                  # Project documentation
└── CONTRIBUTING.md                            # Contribution guidelines
```

---

## Prerequisites

Before running the tests, ensure you have the following installed:

### Required Software

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.8 or higher**
   ```bash
   mvn -version
   ```

3. **Node.js and npm** (for Appium)
   ```bash
   node -v
   npm -v
   ```

4. **Appium Server**
   ```bash
   npm install -g appium
   appium --version
   ```

5. **Appium Drivers**
   ```bash
   # For Android
   appium driver install uiautomator2

   # For iOS (macOS only)
   appium driver install xcuitest
   ```

### Platform-Specific Requirements

#### For Android Testing:
- **Android SDK** (Android Studio or command-line tools)
- **Android Emulator** or real Android device
- Set `ANDROID_HOME` environment variable

#### For iOS Testing (macOS only):
- **Xcode** (latest version)
- **Xcode Command Line Tools**
- **iOS Simulator** or real iOS device
- **WebDriverAgent** (automatically installed by Appium)

---

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/mobile-automation-framework.git
cd mobile-automation-framework
```

### 2. Install Dependencies
```bash
mvn clean install -DskipTests
```

### 3. Start Appium Server
```bash
appium --log-level info
```

Or start Appium in the background:
```bash
appium --log-level info > appium.log 2>&1 &
```

### 4. Verify Installation
```bash
mvn test -Dtest=LoginTest#testLoginPageElements
```

---

## Configuration

### config.properties

Edit `src/main/resources/config.properties` to configure:

```properties
# Appium Server
appium.server.url=http://127.0.0.1:4723

# Android Configuration
android.device.name=Pixel_7_API_34
android.platform.version=14.0
android.app.path=src/test/resources/apps/sample-android-app.apk

# iOS Configuration
ios.device.name=iPhone 15 Pro
ios.platform.version=17.0
ios.app.path=src/test/resources/apps/sample-ios-app.app

# Execution Mode (local or browserstack)
execution.mode=local

# Wait Configuration
implicit.wait=10
explicit.wait=30

# Parallel Execution
thread.count=3
```

### Runtime Configuration Override

You can override any configuration property at runtime without editing the config file:

```bash
# Override device configuration
mvn test -Dandroid.device.name="Pixel_8_API_34" -Dandroid.platform.version="15.0"

# Override Appium server URL
mvn test -Dappium.server.url="http://127.0.0.1:4724"

# Override execution mode
mvn test -Dexecution.mode=browserstack

# Combine multiple overrides
mvn test -Dandroid.device.name="Galaxy_S23" \
         -Dappium.server.url="http://127.0.0.1:4725" \
         -Dthread.count=5
```

### testng.xml

Configure test execution in `testng.xml`:
- Enable/disable test suites
- Set parallel execution mode
- Configure platform parameters
- Group tests by functionality

---

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Android Tests Only
```bash
mvn clean test -Dplatform=android
```

### Run iOS Tests Only
```bash
mvn clean test -Dplatform=ios
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=LoginTest
```

### Run Specific Test Method
```bash
mvn clean test -Dtest=LoginTest#testValidLogin
```

### Run with Custom TestNG File
```bash
mvn clean test -DsuiteXmlFile=smoke-tests.xml
```

### Run with Custom Thread Count
```bash
mvn clean test -DthreadCount=5
```

### Run Tests in Parallel
```bash
mvn clean test -Dparallel=methods -DthreadCount=3
```

### Run on Different Devices Without Editing Config

```bash
# Device 1 - Using defaults from config.properties
mvn clean test

# Device 2 - Override device at runtime
mvn clean test -Dandroid.device.name="emulator-5556" \
                -Dappium.server.url="http://127.0.0.1:4724"

# Real iOS Device - Override with UDID
mvn clean test -Dplatform=ios \
                -Dios.device.name="iPhone 15" \
                -Dios.udid="00008030-001234567890"
```

---

## BrowserStack Integration

The framework supports running tests on BrowserStack cloud platform without any code changes.

### Setup BrowserStack

#### 1. Upload Your App to BrowserStack

```bash
curl -u "USERNAME:ACCESS_KEY" \
  -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
  -F "file=@/path/to/your/app.apk"
```

Response will contain your app URL:
```json
{
  "app_url": "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c"
}
```

#### 2. Configure BrowserStack Credentials

Edit `src/main/resources/config.properties`:

```properties
# Set execution mode
execution.mode=browserstack

# BrowserStack credentials
browserstack.username=your_username
browserstack.access.key=your_access_key

# BrowserStack app URLs
browserstack.android.app.url=bs://your-android-app-id
browserstack.ios.app.url=bs://your-ios-app-id

# Optional: BrowserStack device configuration
browserstack.android.device=Google Pixel 7
browserstack.android.os.version=13.0
browserstack.ios.device=iPhone 14
browserstack.ios.os.version=16
```

### Running Tests on BrowserStack

#### Using config.properties
```bash
# Just run tests - framework will use BrowserStack based on execution.mode
mvn clean test
```

#### Using Runtime Parameters (No config edit needed)
```bash
# Run on BrowserStack with runtime override
mvn clean test -Dexecution.mode=browserstack \
                -Dbrowserstack.username=your_username \
                -Dbrowserstack.access.key=your_key \
                -Dbrowserstack.android.app.url=bs://your-app-id

# Run on specific BrowserStack device
mvn clean test -Dexecution.mode=browserstack \
                -Dbrowserstack.android.device="Samsung Galaxy S23" \
                -Dbrowserstack.android.os.version="13.0"

# Run iOS tests on BrowserStack
mvn clean test -Dplatform=ios \
                -Dexecution.mode=browserstack \
                -Dbrowserstack.ios.device="iPhone 15 Pro" \
                -Dbrowserstack.ios.os.version="17"
```

### BrowserStack Features Included

- **Video Recording** - Automatically records test execution
- **Network Logs** - Captures network traffic for debugging
- **Debug Mode** - Enhanced debugging with visual logs
- **Console Logs** - Captures app console errors
- **Build Organization** - Groups tests by project and build
- **Parallel Execution** - Run tests on multiple devices simultaneously

### Viewing BrowserStack Results

After test execution, view results at:
- **Dashboard**: https://app-automate.browserstack.com/dashboard
- **Builds**: All test runs organized by build name
- **Videos**: Recorded videos of test execution
- **Logs**: Detailed logs and network traffic

### BrowserStack + Parallel Execution

Run tests on multiple BrowserStack devices in parallel:

```bash
mvn clean test -Dexecution.mode=browserstack -DthreadCount=5
```

This will execute tests on up to 5 BrowserStack devices simultaneously (based on your BrowserStack plan).

---

## Test Reporting

### ExtentReports

After test execution, ExtentReports are generated in the `reports/` directory:

```
reports/
├── Mobile-Automation-Test-Report_20250117_143025.html
└── screenshots/
    ├── testValidLogin_FAILED_20250117_143028.png
    └── testFormSubmission_PASSED_20250117_143032.png
```

**Features:**
- Dashboard with test statistics
- Test case details with logs
- Screenshots attached to failed tests
- Execution timeline
- System information
- Filterable test results

### Viewing Reports

1. Navigate to `reports/` directory
2. Open the HTML report in a browser
3. View test execution details, screenshots, and logs

### Log Files

Detailed logs are available in the `logs/` directory:

```
logs/
├── mobile-automation.log          # All logs
├── mobile-automation-error.log    # Error logs only
└── test-execution.log             # Test execution logs
```

---

## CI/CD Integration

### Azure DevOps Pipeline

The framework includes a ready-to-use Azure DevOps pipeline (`azure-pipelines.yml`):

**Pipeline Stages:**
1. **Build** - Compiles the project and resolves dependencies
2. **Android Tests** - Runs Android test suite
3. **iOS Tests** - Runs iOS test suite (optional)
4. **Report Generation** - Publishes test reports and artifacts

**Artifacts Published:**
- Test reports (ExtentReports)
- Screenshots
- Logs
- JUnit XML results

### Running in CI/CD

1. Push code to your Azure DevOps repository
2. Pipeline triggers automatically on commits to `main`, `develop`, or `feature/*` branches
3. View test results in the pipeline run
4. Download artifacts for detailed analysis

### Customization

Modify `azure-pipelines.yml` to:
- Change trigger branches
- Adjust thread count
- Enable/disable iOS tests
- Configure Appium version
- Add additional stages

---

## Best Practices

This framework demonstrates the following best practices:

### Design Patterns
- **Page Object Model (POM)** for separation of concerns
- **Singleton Pattern** for ConfigReader and DriverManager
- **Factory Pattern** for driver initialization
- **Builder Pattern** for test data creation

### Coding Standards
- Comprehensive inline documentation
- Meaningful variable and method names
- Proper exception handling
- Logging at appropriate levels
- DRY (Don't Repeat Yourself) principle

### Test Organization
- Logical grouping of test cases
- Priority-based test execution
- Clear test descriptions
- Independent and isolated tests
- Proper setup and teardown

### Maintainability
- Centralized configuration
- Reusable utilities
- Data-driven approach
- Version-controlled test data
- Modular architecture

---

## Sample Test Execution Output

```
[INFO] Running com.mobile.tests.LoginTest
[INFO] 14:30:25 - Test Started: testValidLogin
[INFO] 14:30:26 - Initializing driver for platform: android
[INFO] 14:30:30 - Driver initialized successfully
[INFO] 14:30:31 - TEST STEP: Verifying login page is displayed
[INFO] 14:30:32 - ASSERTION PASSED: Login page is displayed
[INFO] 14:30:33 - TEST STEP: Performing login with valid credentials
[INFO] 14:30:35 - Clicked on element successfully
[INFO] 14:30:36 - ASSERTION PASSED: Home page is displayed after login
[INFO] 14:30:37 - Test Passed: testValidLogin
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

---

## Contributing

We welcome contributions! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Code of conduct
- Development workflow
- Pull request process
- Coding standards
- Testing guidelines

---

## Troubleshooting

### Common Issues

**Issue: Appium server connection refused**
```
Solution: Ensure Appium server is running on the configured port (default: 4723)
```

**Issue: Element not found**
```
Solution:
- Verify element locators are correct
- Increase wait timeouts in config.properties
- Check if app version has changed
```

**Issue: Driver initialization failed**
```
Solution:
- Verify device/emulator is running
- Check ANDROID_HOME or Xcode installation
- Ensure Appium drivers are installed
```

**Issue: Tests pass locally but fail in CI/CD**
```
Solution:
- Check CI/CD agent configuration
- Verify Appium and driver versions
- Review timeout settings
- Check device availability
```

**Issue: How to run on different devices without editing config.properties?**
```
Solution: Use runtime parameters to override configuration
mvn test -Dandroid.device.name="Device_Name" -Dappium.server.url="http://127.0.0.1:4724"
```

**Issue: How to switch between local and BrowserStack execution?**
```
Solution: Override execution mode at runtime
Local: mvn test -Dexecution.mode=local
BrowserStack: mvn test -Dexecution.mode=browserstack
```

**Issue: BrowserStack authentication failed**
```
Solution:
- Verify browserstack.username and browserstack.access.key in config.properties
- Check your BrowserStack account is active
- Ensure app is uploaded to BrowserStack and app URL is correct
```

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Contact & Support

- **Author**: Mobile Automation Team
- **Email**: automation@example.com
- **Issues**: [GitHub Issues](https://github.com/yourusername/mobile-automation-framework/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/mobile-automation-framework/discussions)

---

## Acknowledgments

- Appium Team for the excellent mobile automation tool
- TestNG Team for the powerful testing framework
- ExtentReports for beautiful test reports
- Open Source Community for continuous support

---

**Happy Testing!** 🚀📱
