# Enterprise Playwright Java Framework

A production-ready, FAANG-compliant test automation framework built with Playwright and Java 21. Enterprise-grade testing infrastructure with comprehensive reporting, CI/CD integration, and cloud-native deployment.

## Features

- **Page Object Model (POM)** - Clean separation of concerns with fluent API
- **Parallel Execution** - Multi-threaded test runs with thread-safe browser management
- **Advanced Waits** - Playwright's auto-waiting + custom explicit wait strategies
- **Data-Driven Testing** - Support for Excel, CSV, JSON test data
- **API Testing** - Integrated REST Assured for end-to-end scenarios
- **Allure Reporting** - Beautiful test reports with screenshots and logs
- **Multi-Browser** - Chromium, Firefox, WebKit support
- **CI/CD Ready** - Jenkins and GitHub Actions workflows included
- **Docker & Kubernetes** - Containerized execution and orchestration
- **Comprehensive Logging** - Structured logging with Log4j2

## Project Structure

\`\`\`
playwright-java-framework/
├── src/
│   ├── main/java/com/enterprise/automation/
│   │   ├── browser/              # Playwright factory and context management
│   │   ├── config/               # Configuration management
│   │   ├── pages/                # Page Object Models
│   │   ├── util/                 # Utilities (Wait, Assert, File, Data)
│   │   └── listener/             # TestNG listeners
│   ├── test/java/com/enterprise/automation/
│   │   └── tests/                # Test cases
│   └── test/resources/
│       ├── env.properties         # Environment configuration
│       ├── playwright.properties   # Browser settings
│       ├── log4j2.properties      # Logging configuration
│       └── testng.xml             # TestNG suite configuration
├── pom.xml                        # Maven configuration
├── Dockerfile                     # Docker image for tests
├── docker-compose.yml             # Multi-container setup
└── k8s/                          # Kubernetes manifests
\`\`\`

## Prerequisites

- **Java**: 21 or higher
- **Maven**: 3.8.0 or higher
- **Git**: For version control

## Installation

### 1. Clone the Repository

\`\`\`bash
git clone https://github.com/yourusername/playwright-java-framework.git
cd playwright-java-framework
\`\`\`

### 2. Install Dependencies

\`\`\`bash
mvn clean install
\`\`\`

This will:
- Download all dependencies
- Compile the project
- Install Playwright browsers

### 3. Verify Installation

\`\`\`bash
mvn test -Dtest=LoginTests
\`\`\`

## Configuration

### Environment Variables

Edit `src/test/resources/env.properties`:

```properties
# Test URLs
qa.url=https://qa.example.com
uat.url=https://uat.example.com
prod.url=https://prod.example.com

# Test Credentials
test.username=user@example.com
test.password=SecurePassword123
