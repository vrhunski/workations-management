# 🌍 Workations Management System

A full-stack application for managing employee workations (work + vacation) with risk assessment based on trip duration. Built with Spring Boot, Angular, and PostgreSQL.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## ✨ Features

- **CRUD Operations**: Create, Read, Update, and Delete workation records
- **Pagination & Sorting**: Efficiently browse through large datasets
- **Advanced Filtering**: Filter by employee, country, destination, and risk level
- **Risk Assessment**: Automatic risk calculation based on trip duration
  - 🟢 NO_RISK: 0-50 days
  - 🟡 LOW_RISK: 51-100 days
  - 🔴 HIGH_RISK: 100+ days
- **Modern UI**: Responsive Angular interface with Material Design
- **Data Validation**: Comprehensive input validation on both frontend and backend
- **CSV Import**: Automatic data import from CSV on startup
- **REST API**: Well-documented RESTful endpoints

## 🛠 Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database**
- **MapStruct** (DTO mapping)
- **Lombok** (Boilerplate reduction)
- **JUnit 5** (Testing)

### Frontend
- **Angular 20.3.0**
- **TypeScript 5.9.2**
- **RxJS 7.8.0**
- **Angular Material 20.2.7**
- **Karma & Jasmine** (Testing)

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required
- **Java Development Kit (JDK) 21** or higher
  ```bash
  java -version
  ```
- **Node.js 20.x** or higher
  ```bash
  node -v
  ```
- **npm 10.x** or higher
  ```bash
  npm -v
  ```
- **H2 Database
  

### Optional
- **Maven 3.9+** (included via Maven Wrapper)
- **Docker & Docker Compose** (for containerized setup)
- **Git** (for version control)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd demonic
```

