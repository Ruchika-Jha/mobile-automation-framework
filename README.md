# Mobile Automation Framework

A comprehensive mobile automation testing framework using Appium, Java, TestNG, and Maven. Supports both **local execution** with Docker/Appium and **cloud execution** with BrowserStack.

## 🚀 Quick Start

### Option 1: Docker (Local Execution - Recommended)

**Prerequisites:** Docker Desktop installed

```bash
# 1. Build Docker images (first time only)
./run-docker-tests.sh android build

# 2. Connect your Android device (enable USB debugging)
adb devices

# 3. Run tests
./run-docker-tests.sh android
```

📖 **Full Docker Guide:** [DOCKER_QUICK_START.md](./DOCKER_QUICK_START.md)

### Option 2: BrowserStack (Cloud Execution)

```bash
# Run tests on BrowserStack cloud
mvn clean test -Dexecution.mode=browserstack -Dplatform=android
mvn clean test -Dexecution.mode=browserstack -Dplatform=ios
```

## 📦 What's Included

### Test Suites
- **LoginTest** (6 tests) - Login functionality validation
- **ProductTest** (8 tests) - Product browsing, cart, and navigation
- **Total: 14 tests** - All working with Sauce Labs Demo App

### Framework Features
- ✅ Page Object Model (POM) design pattern
- ✅ Cross-platform support (Android & iOS)
- ✅ BrowserStack cloud integration
- ✅ Docker local execution
- ✅ ExtentReports HTML reporting
- ✅ Screenshot capture on failure
- ✅ Data-driven testing with JSON
- ✅ Parallel test execution
- ✅ Test retry mechanism
- ✅ Descriptive test naming
- ✅ Dynamic build naming with timestamps

## 🏗️ Project Structure

```
mobile-automation-framework/
├── src/
│   ├── main/java/com/mobile/framework/
│   │   ├── config/          # Configuration management
│   │   ├── listeners/       # TestNG listeners
│   │   ├── pages/           # Page Object classes
│   │   └── utils/           # Utility classes
│   └── test/java/com/mobile/tests/
│       ├── BaseTest.java    # Base test class
│       ├── LoginTest.java   # Login tests
│       └── ProductTest.java # Product tests
├── reports/                 # ExtentReports (auto-generated)
├── logs/                    # Test execution logs
├── Dockerfile              # Docker configuration
├── docker-compose.yml      # Docker services
├── run-docker-tests.sh     # Docker test runner
├── testng.xml              # TestNG suite configuration
└── pom.xml                 # Maven dependencies

```

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 11 |
| Build Tool | Maven | 3.9.x |
| Test Framework | TestNG | 7.10.2 |
| Mobile Automation | Appium | 8.6.0 |
| Reporting | ExtentReports | 5.1.2 |
| Logging | Log4j2 | 2.24.1 |
| Cloud Platform | BrowserStack | - |
| Containerization | Docker | Latest |

## 📊 Test Execution

### Local Execution (Docker)

```bash
# Android
./run-docker-tests.sh android

# iOS (requires macOS and Xcode)
./run-docker-tests.sh ios
```

### BrowserStack Execution

```bash
# All tests - Android
mvn clean test -Dexecution.mode=browserstack -Dplatform=android

# All tests - iOS
mvn clean test -Dexecution.mode=browserstack -Dplatform=ios

# Specific test class
mvn test -Dtest=LoginTest -Dexecution.mode=browserstack -Dplatform=android

# Specific test method
mvn test -Dtest=LoginTest#testValidLogin -Dexecution.mode=browserstack
```

## 📈 Reports

### ExtentReports
After test execution, find HTML reports in `reports/` directory:
```bash
open reports/Mobile-Automation-Test-Report_*.html
```

**Report Features:**
- Test execution dashboard
- Pass/Fail statistics with charts
- Detailed test logs
- Screenshots on failure
- Execution timeline
- Test categorization

### BrowserStack Dashboard
Access your BrowserStack dashboard:
- URL: https://app-automate.browserstack.com/dashboard
- See video recordings, logs, and screenshots
- Descriptive test names
- Dynamic build names with timestamps

## ⚙️ Configuration

### config.properties

Main configuration file: `src/main/resources/config.properties`

Key configurations:
- Appium server URL
- BrowserStack credentials
- Device capabilities
- App URLs
- Timeout values
- Retry counts

### testng.xml

TestNG suite configuration with:
- Parallel execution settings
- Test grouping
- Platform parameters
- Listener configuration

## 🔧 Setup Instructions

### Prerequisites

**For Local Execution:**
- Java 11 or higher
- Maven 3.6+
- Docker Desktop
- Android device with USB debugging OR Android emulator

**For BrowserStack:**
- BrowserStack account
- Valid access key

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd mobile-automation-framework
   ```

2. **Update configuration**
   ```bash
   # Edit src/main/resources/config.properties
   # Add your BrowserStack credentials (if using cloud)
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **For Docker execution**
   ```bash
   ./run-docker-tests.sh android build
   ```

## 🎯 Test Coverage

### LoginTest
- ✅ Valid login with correct credentials
- ✅ Invalid login with wrong credentials
- ✅ Empty fields validation
- ✅ Data-driven login scenarios
- ✅ Forgot password functionality
- ✅ Login page element verification

### ProductTest
- ✅ Products page display
- ✅ Product selection and clicking
- ✅ Menu navigation
- ✅ Cart functionality
- ✅ Sort functionality
- ✅ Logout from products page
- ✅ Multiple product interactions
- ✅ Page element validation

## 🐛 Debugging

### View Logs
```bash
# Check test execution logs
cat logs/mobile-automation.log

# View Docker logs
docker-compose logs appium

# Real-time Appium logs
docker-compose logs -f appium
```

### Common Issues

1. **Port 4723 already in use**
   ```bash
   lsof -i :4723
   kill -9 <PID>
   ```

2. **Device not detected**
   ```bash
   adb devices
   adb kill-server && adb start-server
   ```

3. **BrowserStack authentication failed**
   - Verify credentials in config.properties
   - Check BrowserStack account status

## 📚 Documentation

- [Docker Setup Guide](./DOCKER_SETUP.md) - Detailed Docker documentation
- [Docker Quick Start](./DOCKER_QUICK_START.md) - Fast Docker setup
- [BrowserStack Setup](./BROWSERSTACK_SETUP.md) - Cloud execution guide

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

Mobile Automation Team

## 🙏 Acknowledgments

- Appium community
- TestNG framework
- BrowserStack platform
- Sauce Labs Demo App

---

**Happy Testing! 🚀**

For questions or issues, please open an issue on GitHub.
