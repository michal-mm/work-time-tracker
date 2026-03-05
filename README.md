# Work Time Tracker

A command-line application for tracking work hours with JSON-based persistence.

## Project Structure

```
work-time-tracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── worktimetracker/
│   │   │           ├── boundary/      # CLI interface
│   │   │           ├── control/       # Business logic (Controller, Calculator, Validator)
│   │   │           ├── entity/        # Domain objects (DayFile, TimeEntry, enums)
│   │   │           ├── storage/       # JSON persistence
│   │   │           └── util/          # Utility classes
│   │   └── resources/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── worktimetracker/
│       │           ├── boundary/      # CLI tests
│       │           ├── control/       # Control layer tests
│       │           ├── entity/        # Entity tests
│       │           ├── integration/   # End-to-end tests
│       │           └── storage/       # Storage tests
│       └── resources/
└── pom.xml
```

## Technology Stack

- **Java 25** (JDK with preview features enabled)
- **Maven 3.9+** (build management)
- **Jackson 2.18+** (JSON serialization)
- **JUnit 5** (unit testing)
- **JUnit QuickCheck** (property-based testing)
- **Mockito** (mocking framework)
- **AssertJ** (fluent assertions)

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Creating Executable JAR

```bash
mvn clean package
```

This creates `target/work-time-tracker.jar` which can be executed with:

```bash
java -jar target/work-time-tracker.jar [START|STOP]
```

## Architecture

The application follows the BCE (Boundary-Control-Entity) pattern:

- **Boundary Layer**: Command-line interface for user interactions
- **Control Layer**: Business logic orchestration and validation
- **Entity Layer**: Domain objects and data structures
- **Storage Layer**: JSON file persistence

## Requirements

- Java 25 or higher
- Maven 3.9 or higher
Java app to track work effort spent in the office and at home. It provides stats of real time spent at work vs time expected to work. It allow to add public holidays, vacation time, etc.
