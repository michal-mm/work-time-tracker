# Implementation Plan: Work Time Tracker

## Overview

This implementation plan breaks down the Work Time Tracker into discrete coding tasks following the BCE (Boundary-Control-Entity) architecture. The system will be built in Java 25 using Maven, with Jackson for JSON serialization and JUnit QuickCheck for property-based testing. Tasks are organized to build incrementally from entities through storage, control, and boundary layers, with testing integrated throughout.

## Tasks

- [x] 1. Set up project structure and dependencies
  - Create Maven project with Java 25 configuration
  - Add Jackson dependencies (databind, datatype-jsr310)
  - Add testing dependencies (JUnit 6, JUnit QuickCheck, Mockito, AssertJ)
  - Configure Maven plugins (compiler, surefire, shade)
  - Create package structure: entity, storage, control, boundary, util
  - _Requirements: 9.1, 9.2_

- [ ] 2. Implement Entity Layer
  - [x] 2.1 Create enumeration types
    - Implement DayType enum with all seven values (PUBLIC_HOLIDAY, REGULAR, SICK_LEAVE, TIME_OFF, VACATION, VACATION_FOR_SATURDAY_HOLIDAY, WEEKEND)
    - Implement WorkPlace enum with three values (OFFICE, HOME, CLIENT)
    - Add Jackson annotations for JSON serialization
    - _Requirements: 12.1, 12.2, 12.3, 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7_
  
  - [x]* 2.2 Write unit tests for enumerations
    - Test enum value parsing from JSON
    - Test enum serialization to JSON
    - Test invalid enum value handling
    - _Requirements: 12.1-12.5, 13.1-13.8_
  
  - [x] 2.3 Implement TimeEntry entity
    - Create TimeEntry class with all fields (id, start, end, totalCalculated, totalUsedForReporting, place, paidOverhours)
    - Add Jackson annotations for JSON serialization
    - Implement validation methods for field constraints
    - Add equals/hashCode/toString methods
    - _Requirements: 1.2, 1.3, 9.3, 12.4, 15.1, 15.3, 15.4_
  
  - [x] 2.4 Write property test for TimeEntry
    - **Property 10: End Timestamp After Start**
    - **Validates: Requirements 6.1**
  
  - [x] 2.5 Write property test for TimeEntry
    - **Property 5: Time Entry Consistency**
    - **Validates: Requirements 6.1**
  
  - [ ] 2.6 Implement DayFile entity
    - Create DayFile class with fields (entries, pensum, dayType)
    - Implement getNextEntryId() method
    - Implement getActiveEntry() method
    - Implement getTotalWorkedTime() method
    - Add Jackson annotations for JSON serialization
    - Add equals/hashCode/toString methods
    - _Requirements: 1.2, 3.2, 9.4, 13.8, 14.1, 14.3_
  
  - [ ]* 2.7 Write unit tests for DayFile
    - Test getNextEntryId() with empty and populated entry lists
    - Test getActiveEntry() detection
    - Test getTotalWorkedTime() calculation
    - _Requirements: 1.2, 3.2_

- [ ] 3. Implement Utility Components
  - [ ] 3.1 Implement TimeCalculator
    - Create TimeCalculator class with calculateDuration() method
    - Implement formatDuration() to convert Duration to HH:MM string
    - Implement parseDuration() to convert HH:MM string to Duration
    - Implement sumDurations() for aggregating multiple durations
    - _Requirements: 5.1, 5.2, 5.5_
  
  - [ ]* 3.2 Write property test for TimeCalculator
    - **Property 9: Duration Calculation with Minute Precision**
    - **Validates: Requirements 5.5**
  
  - [ ]* 3.3 Write property test for TimeCalculator
    - **Property 1: Duration Calculation Commutativity**
    - **Validates: Requirements 5.1, 5.2**
  
  - [ ]* 3.4 Write unit tests for TimeCalculator
    - Test duration calculation between two timestamps
    - Test HH:MM formatting edge cases (00:00, 23:59)
    - Test duration parsing validation
    - Test sum of multiple durations
    - _Requirements: 5.1, 5.2, 5.5_
  
  - [ ] 3.5 Implement DataValidator
    - Create DataValidator class with validateTimeEntry() method
    - Implement validateDayFile() method
    - Implement hasActiveEntry() method
    - Implement hasOverlappingEntries() method
    - Implement HH:MM pattern validation
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 14.2, 15.2_
  
  - [ ]* 3.6 Write property test for DataValidator
    - **Property 6: No Overlapping Time Entries**
    - **Validates: Requirements 3.4, 6.3**
  
  - [ ]* 3.7 Write property test for DataValidator
    - **Property 11: Same-Day Constraint**
    - **Validates: Requirements 6.2**
  
  - [ ]* 3.8 Write property test for DataValidator
    - **Property 12: Entry ID Uniqueness**
    - **Validates: Requirements 3.2, 6.5**
  
  - [ ]* 3.9 Write property test for DataValidator
    - **Property 16: HH:MM Format Validation**
    - **Validates: Requirements 14.2, 14.5, 15.2, 15.5**
  
  - [ ]* 3.10 Write unit tests for DataValidator
    - Test end after start validation
    - Test same-day constraint validation
    - Test overlapping entry detection
    - Test active entry detection
    - Test ID uniqueness validation
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

- [ ] 4. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 5. Implement Storage Layer
  - [ ] 5.1 Implement StorageManager
    - Create StorageManager class with loadDayFile() method
    - Implement saveDayFile() with atomic file writes
    - Implement dayFileExists() method
    - Implement getFilePath() method using yyyy-MM-dd.json format
    - Configure Jackson ObjectMapper with JSR310 module for LocalDateTime
    - Add error handling for IOException
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 8.1, 8.2, 8.5, 9.1, 9.2, 9.5, 10.1_
  
  - [ ]* 5.2 Write property test for StorageManager
    - **Property 7: File Naming Convention**
    - **Validates: Requirements 4.2**
  
  - [ ]* 5.3 Write property test for StorageManager
    - **Property 8: Persistence and Loading**
    - **Validates: Requirements 4.1, 4.4, 4.5**
  
  - [ ]* 5.4 Write property test for StorageManager
    - **Property 14: JSON Serialization Round-Trip**
    - **Validates: Requirements 9.1, 9.2, 9.3, 9.4, 10.3**
  
  - [ ]* 5.5 Write property test for StorageManager
    - **Property 15: ISO-8601 Timestamp Format**
    - **Validates: Requirements 9.5**
  
  - [ ]* 5.6 Write unit tests for StorageManager
    - Test loading existing day file
    - Test creating new day file when none exists
    - Test atomic file write behavior
    - Test error handling for corrupted JSON
    - Test error handling for file I/O failures
    - _Requirements: 4.1, 4.4, 4.5, 8.1, 8.2, 8.5, 10.1_

- [ ] 6. Implement Control Layer
  - [ ] 6.1 Implement TimeTrackingController
    - Create TimeTrackingController class with startWorkSession() method
    - Implement stopWorkSession() method
    - Implement getCurrentDayFile() method
    - Implement getActiveEntry() method
    - Integrate with StorageManager for persistence
    - Integrate with TimeCalculator for duration calculations
    - Integrate with DataValidator for validation
    - Add business logic for sequential ID assignment
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2, 4.1, 5.1, 5.3, 5.4, 6.4_
  
  - [ ]* 6.2 Write property test for TimeTrackingController
    - **Property 1: START Command Creates Valid Active Entry**
    - **Validates: Requirements 1.1, 1.2, 1.3, 1.4**
  
  - [ ]* 6.3 Write property test for TimeTrackingController
    - **Property 2: START Command Rejects When Active Entry Exists**
    - **Validates: Requirements 1.5, 6.4**
  
  - [ ]* 6.4 Write property test for TimeTrackingController
    - **Property 3: STOP Command Completes Active Entry**
    - **Validates: Requirements 2.1, 2.2, 2.3, 2.4, 5.1, 5.2, 5.3, 5.4**
  
  - [ ]* 6.5 Write property test for TimeTrackingController
    - **Property 4: STOP Command Rejects When No Active Entry**
    - **Validates: Requirements 2.5**
  
  - [ ]* 6.6 Write property test for TimeTrackingController
    - **Property 5: Multiple Sessions Create Separate Entries**
    - **Validates: Requirements 3.1, 3.2, 3.3**
  
  - [ ]* 6.7 Write unit tests for TimeTrackingController
    - Test START command with no active session
    - Test START command rejection with active session
    - Test STOP command with active session
    - Test STOP command rejection with no active session
    - Test multiple START/STOP cycles
    - Test sequential ID assignment
    - _Requirements: 1.1-1.5, 2.1-2.5, 3.1-3.3_

- [ ] 7. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 8. Implement Boundary Layer (CLI)
  - [ ] 8.1 Implement CommandLineInterface
    - Create CommandLineInterface class with execute() method
    - Implement command parsing for START and STOP commands
    - Implement displaySuccess() method for success messages
    - Implement displayError() method for error messages
    - Add input validation for command arguments
    - Integrate with TimeTrackingController
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5, 8.3_
  
  - [ ]* 8.2 Write unit tests for CommandLineInterface
    - Test START command parsing
    - Test STOP command parsing
    - Test invalid command handling
    - Test success message display
    - Test error message display
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_
  
  - [ ] 8.3 Create main application entry point
    - Create Main class with main() method
    - Instantiate all components (StorageManager, TimeCalculator, DataValidator, TimeTrackingController, CommandLineInterface)
    - Wire dependencies together
    - Handle top-level exceptions
    - _Requirements: 7.1, 7.2, 8.1_

- [ ] 9. Implement Error Handling
  - [ ] 9.1 Add user-friendly error messages
    - Create ErrorMessages utility class with constants
    - Implement error message formatting
    - Add context-specific error messages for all error scenarios
    - _Requirements: 8.1, 8.3_
  
  - [ ]* 9.2 Write property test for error handling
    - **Property 13: Error Messages Are User-Friendly**
    - **Validates: Requirements 8.1, 8.2, 8.3, 8.5**
  
  - [ ]* 9.3 Write unit tests for error scenarios
    - Test START with active session error
    - Test STOP with no active session error
    - Test file I/O error handling
    - Test JSON corruption error handling
    - Test data integrity preservation during errors
    - _Requirements: 8.1, 8.2, 8.3, 8.5_

- [ ] 10. Integration Testing
  - [ ]* 10.1 Write end-to-end integration tests
    - Test complete START/STOP flow with file persistence
    - Test multiple sessions per day
    - Test file persistence across simulated restarts
    - Test error recovery scenarios
    - Verify JSON file structure matches schema
    - _Requirements: 1.1-1.5, 2.1-2.5, 3.1-3.4, 4.1-4.5, 10.1-10.5_

- [ ] 11. Build Configuration
  - [ ] 11.1 Configure Maven for executable JAR
    - Configure Maven Shade plugin to create fat JAR
    - Set main class in manifest
    - Configure JAR name as work-time-tracker.jar
    - Add execution permissions configuration
    - _Requirements: 7.1, 7.2_
  
  - [ ] 11.2 Create build and run scripts
    - Create build script (mvn clean package)
    - Create run script for CLI execution
    - Add usage documentation in README
    - _Requirements: 7.1, 7.2, 7.5_

- [ ] 12. Performance Validation
  - [ ]* 12.1 Write performance tests
    - Test START command execution time (< 200ms)
    - Test STOP command execution time (< 200ms)
    - Test file load time (< 50ms)
    - Test file save time (< 50ms)
    - _Requirements: 11.1, 11.2, 11.3, 11.4_

- [ ] 13. Final checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Property-based tests validate universal correctness properties from the design document
- Unit tests validate specific examples and edge cases
- Integration tests verify end-to-end functionality
- The implementation follows BCE architecture: Entity → Storage → Control → Boundary
- All 16 correctness properties from the design are covered by property-based tests
- All 15 requirements with 75 acceptance criteria are covered by implementation tasks
