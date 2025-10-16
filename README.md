# 🌍 Workations Management System

A full-stack enterprise application for managing employee workations (remote work assignments) with intelligent risk assessment. Built with Spring Boot, Angular 20, and H2 in-memory Database.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Angular](https://img.shields.io/badge/Angular-20.3-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Building for Production](#building-for-production)
- [Testing](#testing)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Git Setup](#git-setup)
- [Deployment](#deployment)
- [Support](#support)

## ⚡ Quick Start

```bash
# 1. Clone the repository
git clone <repository-url>
cd demonic

# 2. Start PostgreSQL with Docker
docker-compose up -d

# 3. Run Backend (Terminal 1)
./mvnw spring-boot:run

# 4. Run Frontend (Terminal 2)
cd src/frontend
npm install
npm start

# 5. Open browser
# Backend:  http://localhost:8080
# Frontend: http://localhost:4200
```

## ✨ Features

- **CRUD Operations**: Create, Read, Update, and Delete workation records
- **Pagination & Sorting**: Efficiently browse through large datasets with sortable columns
- **Advanced Filtering**: Filter by employee, country, destination, and risk level
- **Risk Assessment**: Automatic risk calculation based on trip duration
  - 🟢 NO_RISK: 0-50 days
  - 🟡 LOW_RISK: 51-100 days
  - 🔴 HIGH_RISK: 100+ days
- **Modern UI**: Responsive Angular interface with Material Design
- **Data Validation**: Comprehensive input validation on both frontend and backend
- **CSV Import**: Automatic data import from CSV on startup
- **REST API**: Well-documented RESTful endpoints with Swagger
- **Country Flags**: Visual representation with emoji flags
- **Date Formatting**: User-friendly date display (dd/MM/yyyy)
- **Row Highlighting**: Interactive table with hover effects

## 🛠 Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2**
- **MapStruct** (DTO mapping)
- **Lombok** (Boilerplate reduction)
- **JUnit 5** (Testing)
- **Swagger/OpenAPI** (API Documentation)

### Frontend
- **Angular 20.3.0**
- **TypeScript 5.9.2**
- **RxJS 7.8.0**
- **Angular Material 20.2.7**
- **MobX 6.15.0**
- **Karma & Jasmine** (Testing)

## 📦 Prerequisites

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| **Java JDK** | 21+ | [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/) |
| **Node.js** | 20.x+ | [nodejs.org](https://nodejs.org/) |
| **npm** | 10.x+ | Included with Node.js |

### Optional Software

| Software | Purpose | Download Link |
|----------|---------|---------------|
| **Docker Desktop** | Containerized PostgreSQL | [docker.com](https://www.docker.com/products/docker-desktop/) |
| **IntelliJ IDEA** | IDE for Java | [jetbrains.com](https://www.jetbrains.com/idea/download/) |
| **VS Code** | IDE for Frontend | [code.visualstudio.com](https://code.visualstudio.com/) |
| **Postman** | API Testing | [postman.com](https://www.postman.com/downloads/) |

### Verify Installation

```bash
# Check Java version
java -version
# Expected: java version "21.x.x"

# Check Node.js version
node -v
# Expected: v20.x.x

# Check npm version
npm -v
# Expected: 10.x.x

# Check PostgreSQL
psql --version
# Expected: psql (PostgreSQL) 14.x or higher
```

## 🚀 Installation

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
cd demonic
```

### Step 2: Install Backend Dependencies

```bash
# From project root directory
./mvnw clean install

# On Windows:
mvnw.cmd clean install
```

This will:
- Download all Maven dependencies
- Compile the Java code
- Run tests
- Build the JAR file

### Step 3: Install Frontend Dependencies

```bash
# Navigate to frontend directory
cd src/frontend

# Install dependencies
npm install

# Return to project root
cd ../..
```

This will install all Angular dependencies (~300MB, takes 2-5 minutes).

## ▶️ Running the Application

### Development Mode (Recommended for Development)

You need **two terminal windows** open simultaneously.

#### Terminal 1: Start Backend Server

```bash
# From project root
./mvnw spring-boot:run

# On Windows:
mvnw.cmd spring-boot:run
```

**Expected Output:**
```
...
2025-01-16 10:30:45.123  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2025-01-16 10:30:45.234  INFO --- [main] c.w.d.WorkationApplication               : Started WorkationApplication in 3.456 seconds
```

✅ Backend is ready when you see: **"Started WorkationApplication"**

**Access Backend:**
- API Base URL: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health Check: `http://localhost:8080/actuator/health`

#### Terminal 2: Start Frontend Server

```bash
# Navigate to frontend directory
cd src/frontend

# Start development server
npm start

# Alternative command:
npm run start
```

**Expected Output:**
```
** Angular Live Development Server is listening on localhost:4200 **
✔ Compiled successfully.
```

✅ Frontend is ready when you see: **"Compiled successfully"**

**Access Frontend:**
- Application: `http://localhost:4200`
- Auto-reload: Enabled (changes reflect automatically)

### Stop the Application

Press `Ctrl + C` in each terminal to stop the servers.

### Running with Docker Compose (Full Stack) - Recommended for Production

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## 🏗️ Building for Production

### Backend Production Build

```bash
# Build JAR file
./mvnw clean package -DskipTests

# The JAR will be created in:
# target/demonic-0.0.1-SNAPSHOT.jar

# Run the JAR
java -jar target/demonic-0.0.1-SNAPSHOT.jar
```

**Production Configuration:**

Create `src/main/resources/application-prod.properties`:

```properties
server.port=8080
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/workations_db}
spring.datasource.username=${DATABASE_USER:postgres}
spring.datasource.password=${DATABASE_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.workflex.demonic=INFO
```

Run with production profile:
```bash
java -jar -Dspring.profiles.active=prod target/demonic-0.0.1-SNAPSHOT.jar
```

### Frontend Production Build

```bash
# Navigate to frontend
cd src/frontend

# Build for production
npm run build

# Output directory: dist/
# Files are optimized, minified, and ready for deployment
```

**Build Output:**
```
dist/
├── browser/
│   ├── index.html
│   ├── main.js
│   ├── styles.css
│   └── assets/
└── server/ (if SSR enabled)
```

### Deploy Static Files to Backend

Copy frontend build to Spring Boot static resources:

```bash
# From project root
cd src/frontend
npm run build

# Copy build files to backend resources
cp -r dist/browser/* ../../src/main/resources/static/

# Rebuild backend with frontend
cd ../..
./mvnw clean package -DskipTests
```

Now the backend serves both API and frontend:
- Access everything at: `http://localhost:8080`

## 🧪 Testing

### Backend Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=WorkationApplicationTest

# Run tests with coverage
./mvnw clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

**Test Structure:**
- Unit Tests: Service layer logic
- Integration Tests: REST API endpoints
- Repository Tests: Database operations

### Frontend Tests

```bash
cd src/frontend

# Run tests once
npm test

# Run tests in watch mode
npm test -- --watch

# Run with coverage
npm test -- --code-coverage

# View coverage report
open coverage/index.html

# Run specific test
npm test -- --include='**/api.service.spec.ts'
```

**Test Types:**
- Unit Tests: Components and services
- Integration Tests: HTTP requests
- E2E Tests: User workflows (if configured)

### Run All Tests

```bash
# Backend + Frontend tests
./mvnw test && cd src/frontend && npm test && cd ../..
```

## 📚 API Documentation

### OpenAPI/Swagger UI

Access interactive API documentation:

```
http://localhost:8080/swagger-ui.html
```

### Key Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/workations` | Get all workations (paginated) |
| GET | `/api/v1/workations/{id}` | Get workation by ID |
| POST | `/api/v1/workations` | Create new workation |
| PUT | `/api/v1/workations/{id}` | Update workation |
| DELETE | `/api/v1/workations/{id}` | Delete workation |

### Query Parameters

**GET /api/v1/workations:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `sortBy` (optional): Sort field (id, employee, country, country_dest, start_date, end_date, days, risk)
- `sortDirection` (optional): ASC or DESC
- `employee` (optional): Filter by employee name
- `country` (optional): Filter by origin country
- `countryDest` (optional): Filter by destination country
- `risk` (optional): Filter by risk level (NO_RISK, LOW_RISK, HIGH_RISK)

### Example API Requests

```bash
# Get all workations
curl http://localhost:8080/api/v1/workations

# Get with pagination and sorting
curl "http://localhost:8080/api/v1/workations?page=0&size=10&sortBy=employee&sortDirection=ASC"

# Filter by country
curl "http://localhost:8080/api/v1/workations?country=United%20States"

# Create workation
curl -X POST http://localhost:8080/api/v1/workations \
  -H "Content-Type: application/json" \
  -d '{
    "employee": "John Doe",
    "country": "United States",
    "countryDest": "Portugal",
    "startDate": "2025-03-15",
    "endDate": "2025-05-29",
    "days": 75,
    "risk": "LOW_RISK"
  }'

# Update workation
curl -X PUT http://localhost:8080/api/v1/workations/1 \
  -H "Content-Type: application/json" \
  -d '{
    "employee": "John Doe Updated",
    "country": "United States",
    "countryDest": "Spain",
    "startDate": "2025-03-15",
    "endDate": "2025-06-30",
    "days": 107,
    "risk": "HIGH_RISK"
  }'

# Delete workation
curl -X DELETE http://localhost:8080/api/v1/workations/1
```

## ⚙️ Configuration

### Backend Configuration

**File:** `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:h2:mem:workflexdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# CSV Data Import
app.csv.import.enabled=true

# Logging
logging.level.root=INFO
logging.level.com.workflex.demonic=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG

# CORS Configuration (already configured in code)
# Allows all origins for development
```

### Frontend Configuration

**File:** `src/frontend/src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
  apiTimeout: 30000
};
```

**File:** `src/frontend/src/environments/environment.prod.ts`

```typescript
export const environment = {
  production: true,
  apiUrl: '/api/v1',  // Relative URL for same-origin deployment
  apiTimeout: 30000
};
```

### Change Ports

**Backend Port:**

Edit `application.properties`:
```properties
server.port=8081
```

**Frontend Port:**

Edit `src/frontend/angular.json`:
```json
{
  "architect": {
    "serve": {
      "options": {
        "port": 4201
      }
    }
  }
}
```

Or use command line:
```bash
ng serve --port 4201
```

## 📁 Project Structure

```
demonic/
├── src/
│   ├── main/
│   │   ├── java/com/workflex/demonic/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exception handlers
│   │   │   ├── model/               # JPA entities
│   │   │   ├── repository/          # Data repositories
│   │   │   ├── service/             # Business logic
│   │   │   └── WorkationApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── workations.csv       # Initial data
│   │       └── static/              # Frontend build output (prod)
│   ├── test/                        # Backend tests
│   └── frontend/                    # Angular application
│       ├── src/
│       │   ├── app/
│       │   │   ├── workations/      # Main feature
│       │   │   ├── shared/          # Shared services/components
│       │   │   ├── core/            # Core modules
│       │   │   └── about/           # About page
│       │   ├── assets/              # Static assets
│       │   └── environments/        # Environment configs
│       ├── angular.json             # Angular CLI config
│       ├── package.json             # NPM dependencies
│       └── tsconfig.json            # TypeScript config
├── target/                          # Build output
├── docker-compose.yaml              # Docker configuration
├── pom.xml                          # Maven configuration
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
└── README.md                        # This file
```

## 🐛 Troubleshooting

### Port Already in Use

#### Backend (Port 8080)

**macOS/Linux:**
```bash
lsof -ti:8080 | xargs kill -9
```

**Windows:**
```cmd
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

#### Frontend (Port 4200)

**macOS/Linux:**
```bash
lsof -ti:4200 | xargs kill -9
```

**Windows:**
```cmd
netstat -ano | findstr :4200
taskkill /PID <PID> /F
```

2. **Test connection:**

```bash
psql -h localhost -U postgres -d workations_db
```

3. **Check credentials in `application.properties`**


### Maven Build Failures

```bash
# Clear Maven cache
./mvnw clean

# Force update dependencies
./mvnw clean install -U

# Skip tests if they're failing
./mvnw clean install -DskipTests

# Run with debug output
./mvnw clean install -X
```

### NPM Install Failures

```bash
cd src/frontend

# Clear npm cache
npm cache clean --force

# Delete node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall
npm install

# If still failing, try:
npm install --legacy-peer-deps
```

### Frontend Build Errors

```bash
# Clear Angular cache
cd src/frontend
rm -rf .angular/cache

# Rebuild
npm run build

# If TypeScript errors, check versions
npx tsc --version
```

### Backend Not Starting

1. **Check Java version:**
```bash
java -version
# Must be 21 or higher
```

2. **Check port conflicts:**
```bash
lsof -i:8080
```

3. **View full logs:**
```bash
./mvnw spring-boot:run -X
```

4. **Check database connection in logs**

### Frontend Not Loading

1. **Check console for errors** (F12 in browser)

2. **Verify backend is running:**
```bash
curl http://localhost:8080/api/v1/workations
```

3. **Check CORS configuration** in `WorkationController.java`

4. **Clear browser cache:** Ctrl+Shift+R (Chrome) or Cmd+Shift+R (Mac)

### Data Not Loading

1. **Check CSV file exists:**
```bash
ls -la src/main/resources/workations.csv
```

2. **Check import logs:**
   Look for "Loading workation data from CSV" in console

3. **Check `application.properties`:**
```properties
app.csv.import.enabled=true
```

## 🎯 Git Setup

### Initialize and Push to Bitbucket

```bash
# Initialize git (if not already)
git init

# Configure user
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Add all files
git add .

# Create initial commit
git commit -m "Initial commit: Workations Management System"

# Add remote repository
git remote add origin https://bitbucket.org/your-username/workations-management.git

# Push to Bitbucket
git push -u origin main
```

### Daily Git Workflow

```bash
# Check status
git status

# Add changes
git add .

# Commit
git commit -m "Description of changes"

# Push
git push

# Pull latest
git pull
```

## 🚀 Deployment

### Deploy to Heroku

```bash
# Login to Heroku
heroku login

# Create app
heroku create your-app-name

# Add PostgreSQL
heroku addons:create heroku-postgresql:hobby-dev

# Deploy
git push heroku main

# Open app
heroku open
```

### Deploy to Docker

```bash
# Build images
docker-compose build

# Push to registry
docker tag demonic-backend:latest your-registry/demonic-backend
docker push your-registry/demonic-backend

# Deploy to production
docker-compose -f docker-compose.prod.yml up -d
```

## 🎯 Development Workflow

### Daily Development

1. **Start Database** (once):
```bash
docker-compose up -d
```

2. **Start Backend** (Terminal 1):
```bash
./mvnw spring-boot:run
```

3. **Start Frontend** (Terminal 2):
```bash
cd src/frontend && npm start
```

4. **Make changes** - Both will auto-reload!

5. **Test changes** - Navigate to `http://localhost:4200`

### Before Committing

```bash
# Run all tests
./mvnw test
cd src/frontend && npm test

# Build production
./mvnw clean package -DskipTests
cd src/frontend && npm run build
```

## 📞 Support

- **Issues:** [GitHub/Bitbucket Issues](your-repo/issues)
- **Documentation:** This README
- **API Docs:** http://localhost:8080/swagger-ui.html

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

- Branislav Vrtunski - Initial work

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Angular team for the powerful frontend framework
- MapStruct for simplifying DTO mapping
- The open-source community

---

**Made with ❤️ using Spring Boot & Angular**

**Happy Coding! 🚀**
```