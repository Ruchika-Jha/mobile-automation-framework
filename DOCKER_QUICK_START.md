# Docker Quick Start Guide

## 🚀 Get Started in 3 Steps

### Step 1: Build (First Time Only)
```bash
./run-docker-tests.sh android build
```

### Step 2: Connect Your Device
- **Android**: Enable USB debugging and connect via USB
- Verify: `adb devices`

### Step 3: Run Tests
```bash
./run-docker-tests.sh android
```

## 📊 View Results

After tests complete, open the report:
```bash
open reports/Mobile-Automation-Test-Report_*.html
```

## 🔄 Common Commands

| Task | Command |
|------|---------|
| Run Android tests | `./run-docker-tests.sh android` |
| Run iOS tests | `./run-docker-tests.sh ios` |
| Rebuild images | `./run-docker-tests.sh android build` |
| Stop all containers | `docker-compose down` |
| View Appium logs | `docker-compose logs appium` |
| Clean everything | `docker-compose down -v && docker system prune -a` |

## 🎯 What Gets Created

```
mobile-automation-framework/
├── Dockerfile              # Main container image
├── docker-compose.yml      # Services configuration
├── run-docker-tests.sh     # Easy test runner script
├── DOCKER_SETUP.md        # Detailed documentation
└── reports/               # Test results (auto-generated)
```

## ⚡ Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Port 4723 already in use | `lsof -i :4723` then kill the process |
| Device not detected | Check `adb devices` |
| Docker not running | Start Docker Desktop |
| Permission denied | `chmod +x run-docker-tests.sh` |

## 📖 Need More Help?

Read the full documentation: [DOCKER_SETUP.md](./DOCKER_SETUP.md)
