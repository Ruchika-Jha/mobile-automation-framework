# Contributing to Mobile Automation Framework

Thank you for your interest in contributing to the Mobile Automation Framework! This document provides guidelines and best practices for contributing to this project.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Pull Request Process](#pull-request-process)
- [Reporting Issues](#reporting-issues)
- [Feature Requests](#feature-requests)

---

## Code of Conduct

### Our Pledge

We are committed to providing a welcoming and inspiring community for all. Please be respectful and considerate in your interactions.

### Expected Behavior

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

### Unacceptable Behavior

- Trolling, insulting/derogatory comments, and personal attacks
- Public or private harassment
- Publishing others' private information without permission
- Other conduct which could reasonably be considered inappropriate

---

## Getting Started

### Prerequisites

Before contributing, ensure you have:

1. **Java JDK 11+** installed
2. **Maven 3.8+** installed
3. **Git** installed and configured
4. **IDE** (IntelliJ IDEA, Eclipse, or VS Code)
5. **Appium** and required drivers
6. Basic knowledge of:
   - Java programming
   - Mobile automation with Appium
   - TestNG framework
   - Maven build tool
   - Page Object Model pattern

### Setting Up Your Development Environment

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/mobile-automation-framework.git
   cd mobile-automation-framework
   ```

3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/ORIGINAL_OWNER/mobile-automation-framework.git
   ```

4. **Install dependencies**:
   ```bash
   mvn clean install
   ```

5. **Verify setup**:
   ```bash
   mvn test -Dtest=LoginTest#testLoginPageElements
   ```

---

## How to Contribute

### Types of Contributions

We welcome various types of contributions:

1. **Bug Fixes** - Fix issues in existing code
2. **New Features** - Add new functionality
3. **Documentation** - Improve or add documentation
4. **Test Cases** - Add or improve test coverage
5. **Performance Improvements** - Optimize existing code
6. **Code Refactoring** - Improve code quality
7. **Configuration** - Enhance configuration options

---

## Development Workflow

### 1. Create a Branch

Always create a new branch for your work:

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b bugfix/issue-number-description
```

**Branch Naming Conventions:**
- `feature/feature-name` - for new features
- `bugfix/issue-123-description` - for bug fixes
- `enhancement/description` - for enhancements
- `docs/description` - for documentation
- `refactor/description` - for code refactoring

### 2. Make Your Changes

- Write clean, readable code
- Follow coding standards (see below)
- Add/update tests as needed
- Update documentation if required
- Add comments for complex logic

### 3. Test Your Changes

Before committing, ensure:

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=YourTestClass

# Check code compiles
mvn clean compile
```

### 4. Commit Your Changes

Write clear, descriptive commit messages:

```bash
git add .
git commit -m "feat: add new login validation test

- Added test for empty username validation
- Updated LoginPage with new validation methods
- Added test data for validation scenarios

Closes #123"
```

**Commit Message Format:**
```
<type>: <subject>

<body>

<footer>
```

**Types:**
- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation changes
- `test` - Adding or updating tests
- `refactor` - Code refactoring
- `style` - Code style changes
- `perf` - Performance improvements
- `chore` - Build/tooling changes

### 5. Push Your Changes

```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

1. Go to GitHub and create a Pull Request
2. Fill in the PR template completely
3. Link related issues
4. Wait for review and address feedback

---

## Coding Standards

### Java Code Style

1. **Naming Conventions**
   ```java
   // Classes: PascalCase
   public class LoginPage extends BasePage { }

   // Methods: camelCase
   public void enterUsername(String username) { }

   // Variables: camelCase
   private WebElement usernameField;

   // Constants: UPPER_SNAKE_CASE
   private static final int DEFAULT_TIMEOUT = 30;
   ```

2. **Indentation**
   - Use 4 spaces (no tabs)
   - Properly indent nested blocks

3. **Line Length**
   - Maximum 120 characters per line
   - Break long lines appropriately

4. **Comments**
   ```java
   /**
    * JavaDoc for public methods
    * Explain what the method does, parameters, and return value
    *
    * @param username The username to enter
    * @return LoginPage instance for method chaining
    */
   public LoginPage enterUsername(String username) {
       // Inline comments for complex logic
       sendKeys(usernameField, username);
       return this;
   }
   ```

5. **Method Organization**
   - Public methods first
   - Protected methods next
   - Private methods last
   - Group related methods together

### Page Object Model Standards

1. **Page Class Structure**
   ```java
   public class LoginPage extends BasePage {
       // Element declarations
       @AndroidFindBy(id = "username")
       private WebElement usernameField;

       // Page actions (public methods)
       public LoginPage enterUsername(String username) { }

       // Validation methods
       public boolean isLoginPageDisplayed() { }

       // Helper methods (private)
       private void waitForPageLoad() { }
   }
   ```

2. **Return Types**
   - Return same page object for actions on same page (method chaining)
   - Return new page object when navigating to another page

3. **No Assertions in Page Objects**
   - Page objects should only contain actions and queries
   - Put assertions in test classes

### Test Class Standards

1. **Test Method Naming**
   ```java
   @Test(description = "Verify user can login with valid credentials")
   public void testValidLogin() { }
   ```

2. **Test Structure** (AAA Pattern)
   ```java
   @Test
   public void testValidLogin() {
       // Arrange - Setup test data and preconditions
       LoginPage loginPage = new LoginPage();

       // Act - Perform the action
       HomePage homePage = loginPage.login("user@test.com", "password");

       // Assert - Verify the outcome
       Assert.assertTrue(homePage.isHomePageDisplayed());
   }
   ```

3. **Test Independence**
   - Each test should be independent
   - Tests should not depend on execution order
   - Use proper setup and teardown

---

## Testing Guidelines

### Writing Tests

1. **Test Coverage**
   - Add tests for new features
   - Add tests for bug fixes
   - Maintain or improve overall coverage

2. **Test Data**
   - Use JSON files for test data
   - Keep test data separate from code
   - Use data providers for data-driven tests

3. **Assertions**
   ```java
   // Good: Descriptive assertion messages
   Assert.assertTrue(homePage.isDisplayed(),
       "Home page should be displayed after successful login");

   // Bad: No message
   Assert.assertTrue(homePage.isDisplayed());
   ```

4. **Test Logging**
   ```java
   logTestStep("Performing login with valid credentials");
   logAssertion("Login successful", result);
   ```

### Running Tests Before PR

```bash
# Run all tests
mvn clean test

# Run tests for specific class
mvn test -Dtest=LoginTest

# Run with coverage
mvn clean test jacoco:report
```

---

## Pull Request Process

### Before Submitting

- [ ] Code follows project coding standards
- [ ] All tests pass locally
- [ ] New tests added for new functionality
- [ ] Documentation updated if needed
- [ ] Commit messages are clear and descriptive
- [ ] Branch is up to date with main branch

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Enhancement
- [ ] Documentation update

## Testing
How were these changes tested?

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Tests pass
- [ ] Documentation updated
- [ ] Code follows style guidelines

## Related Issues
Closes #123
```

### Review Process

1. **Automated Checks** - CI/CD pipeline runs automatically
2. **Code Review** - Maintainers review your code
3. **Feedback** - Address review comments
4. **Approval** - Once approved, PR will be merged
5. **Merge** - Maintainer merges your PR

### Addressing Feedback

```bash
# Make requested changes
git add .
git commit -m "address review feedback: update test assertions"
git push origin feature/your-feature-name
```

---

## Reporting Issues

### Before Reporting

- Check if issue already exists
- Verify it's not a configuration problem
- Test with latest version

### Issue Template

```markdown
## Description
Clear description of the issue

## Steps to Reproduce
1. Step 1
2. Step 2
3. Step 3

## Expected Behavior
What should happen

## Actual Behavior
What actually happens

## Environment
- OS: macOS 14.0
- Java Version: 11
- Appium Version: 2.0.0
- Platform: Android/iOS

## Screenshots/Logs
Add relevant screenshots or log files

## Additional Context
Any other relevant information
```

---

## Feature Requests

We welcome feature requests! Please provide:

1. **Use Case** - Why is this feature needed?
2. **Proposed Solution** - How should it work?
3. **Alternatives** - Any alternative solutions considered?
4. **Additional Context** - Any other relevant information

---

## Style Guide Summary

### Do's ✅

- Write self-documenting code
- Add JavaDoc for public methods
- Use meaningful variable names
- Follow Page Object Model pattern
- Write independent tests
- Log important steps
- Handle exceptions properly
- Use configuration files
- Write modular, reusable code

### Don'ts ❌

- Don't hard-code values
- Don't use magic numbers
- Don't put assertions in page objects
- Don't write dependent tests
- Don't ignore warnings
- Don't skip documentation
- Don't commit generated files
- Don't commit credentials

---

## Questions?

If you have questions about contributing:

1. Check existing documentation
2. Search closed issues
3. Ask in discussions
4. Contact maintainers

---

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- Project documentation

---

## License

By contributing, you agree that your contributions will be licensed under the same license as the project (MIT License).

---

Thank you for contributing to Mobile Automation Framework! 🚀

Your contributions help make this framework better for everyone in the test automation community.
