
# Football Club Management System

A comprehensive database-driven desktop application developed for managing football club operations using Java, PostgreSQL, and JasperReports.

---

# Project Overview

The Football Club Management System was developed as part of the Database course project (Academic Year 2024/2025).

The system provides centralized management for football club operations including:

- Player management
- Training sessions
- Tournament organization
- Game scheduling
- Medical records
- Contracts management
- Professional reporting

The application implements role-based access control with dedicated interfaces for:

- Players
- Head Coaches
- Doctors
- Managers

---

# Main Features

## User Management
- Secure login system
- Role-based access control
- Personalized dashboards

## Player Management
- Player profiles
- Contract management
- Jersey numbers
- Position tracking
- Nationality and health status

## Training & Games
- Training session scheduling
- Match scheduling
- Tactical planning
- Stadium assignment
- Jersey type management

## Tournament Management
- Tournament creation
- Tournament status tracking
- Match organization

## Medical Management
- Injury tracking
- Recovery monitoring
- Health record management

## Reporting
Professional JasperReports integration including:
- Staff Directory Report
- Player Health Report
- Contract Summary Report
- Tournament & Games Report

---

# Technologies Used

## Programming & Development
- Java
- Maven
- Java Swing
- NetBeans IDE

## Database
- PostgreSQL
- SQL

## Reporting & Design
- JasperReports
- JRXML
- Draw.io

---

# Database Design

The database was designed using proper normalization techniques up to BCNF to ensure:

- Data integrity
- Elimination of redundancy
- Efficient data management
- Strong relational consistency

Core entities include:

- Users
- Players
- Head Coaches
- Doctors
- Managers
- Contracts
- Tournaments
- Games Schedule
- Training Sessions
- Injuries
- Achievements

---

# System Architecture

The application follows a modular architecture with clear separation of concerns.

Main components include:

- Authentication Module
- Database Connection Layer
- Role-Based Dashboards
- Entity Management Classes
- Reporting Engine
- Service Layer

---

# Project Structure

```text
FootballClubManagementSystem/
│
├── src/
│   ├── dashboards/
│   ├── database/
│   ├── entities/
│   ├── reports/
│   ├── services/
│   └── ui/
│
├── database/
│   ├── schema.sql
│   └── queries.sql
│
├── reports/
│   ├── jasper/
│   └── jrxml/
│
├── pom.xml
└── README.md
```

---

# Requirements

Install the following before running the project:

- Java JDK 17+
- PostgreSQL
- Maven
- NetBeans IDE (recommended)

---

# Run the Project

## 1. Clone the repository

```bash
git clone <repository-url>
```

## 2. Configure PostgreSQL database
- Create the database
- Import the SQL schema
- Update database credentials inside the project

## 3. Run the application

```bash
mvn clean install
mvn exec:java
```

Or run directly from NetBeans.

---

# Reports

The system generates professional reports using JasperReports:

- Staff Directory Report
- Player Health Report
- Contract Summary Report
- Tournament & Games Management Report

Reports can be exported in:
- PDF
- Excel
- HTML

---

# Key Achievements

- Role-based access control
- BCNF normalized database
- Professional GUI interfaces
- Advanced SQL queries
- Integrated reporting system
- Modular Java architecture
- Scalable and maintainable design

---

# Authors

Database Project — Academic Year 2024/2025 developed by : 


### Hamdi Balati
- GitHub: [@hamdibalati2-Eng](https://github.com/hamdibalati2-Eng)

### AbdAlrahman Abd Alfatah
- GitHub: [@AbdAlrahman-Sec](https://github.com/AbdAlrahman-Sec)
