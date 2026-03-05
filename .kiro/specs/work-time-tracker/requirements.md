# Requirements Document: Work Time Tracker

## Introduction

The Work Time Tracker is a command-line application that enables users to track their work hours by recording start and stop timestamps throughout the day. The system supports multiple work sessions per day to accommodate breaks and interruptions, storing data in JSON files organized by date. This document specifies the functional and non-functional requirements that the system must satisfy.

## Glossary

- **System**: The Work Time Tracker application
- **CLI**: Command-Line Interface component that handles user interactions
- **Controller**: Control layer component that orchestrates business logic
- **Storage_Manager**: Component responsible for JSON file persistence
- **Time_Calculator**: Component that performs time-related calculations
- **Data_Validator**: Component that validates data integrity and business rules
- **Day_File**: Entity representing all time entries for a single calendar day
- **Time_Entry**: Entity representing a single work session with start and optional end timestamp
- **Active_Entry**: A Time_Entry where the end timestamp is null (session in progress)
- **Work_Session**: A period of work time tracked by the system

## Requirements

### Requirement 1: Start Work Session

**User Story:** As a user, I want to start tracking my work time, so that I can record when I begin working.

#### Acceptance Criteria

1. WHEN a user executes the START command, THE System SHALL create a new Time_Entry with the current timestamp as the start time
2. WHEN a user executes the START command, THE System SHALL assign a unique sequential ID to the new Time_Entry
3. WHEN a user executes the START command, THE System SHALL set the end timestamp to null to indicate an active session
4. WHEN a user executes the START command, THE System SHALL persist the Time_Entry to the Day_File for the current date
5. IF an Active_Entry already exists for the current day, THEN THE System SHALL reject the START command and return an error message

### Requirement 2: Stop Work Session

**User Story:** As a user, I want to stop tracking my work time, so that I can record when I finish working.

#### Acceptance Criteria

1. WHEN a user executes the STOP command, THE System SHALL set the end timestamp of the Active_Entry to the current timestamp
2. WHEN a user executes the STOP command, THE System SHALL calculate the duration between start and end timestamps
3. WHEN a user executes the STOP command, THE System SHALL store the calculated duration in HH:MM format
4. WHEN a user executes the STOP command, THE System SHALL persist the updated Time_Entry to the Day_File
5. IF no Active_Entry exists for the current day, THEN THE System SHALL reject the STOP command and return an error message

### Requirement 3: Multiple Sessions Per Day

**User Story:** As a user, I want to track multiple work sessions in a single day, so that I can accommodate breaks and interruptions.

#### Acceptance Criteria

1. WHEN a user completes a Work_Session and starts a new one, THE System SHALL create a separate Time_Entry for each session
2. WHEN multiple Time_Entries exist for a day, THE System SHALL assign sequential IDs starting from 0
3. THE System SHALL allow an unlimited number of completed Work_Sessions per day
4. THE System SHALL ensure no two Time_Entries have overlapping time ranges

### Requirement 4: Data Persistence

**User Story:** As a user, I want my time tracking data to be saved automatically, so that I don't lose my work records.

#### Acceptance Criteria

1. WHEN a Time_Entry is created or modified, THE System SHALL persist it to a JSON file immediately
2. THE Storage_Manager SHALL name files using the format yyyy-MM-dd.json
3. THE Storage_Manager SHALL store files in a designated data directory
4. WHEN the System starts, THE Storage_Manager SHALL load the Day_File for the current date if it exists
5. IF the Day_File does not exist for the current date, THEN THE Storage_Manager SHALL create a new empty Day_File

### Requirement 5: Time Calculation

**User Story:** As a user, I want the system to calculate work durations automatically, so that I don't have to manually compute time differences.

#### Acceptance Criteria

1. WHEN a Work_Session is stopped, THE Time_Calculator SHALL compute the duration as the difference between end and start timestamps
2. THE Time_Calculator SHALL format durations as HH:MM strings
3. THE Time_Calculator SHALL store the calculated duration in the totalCalculated field
4. THE Time_Calculator SHALL store the calculated duration in the totalUsedForReporting field
5. THE System SHALL calculate durations with minute-level precision

### Requirement 6: Data Validation

**User Story:** As a user, I want the system to validate my time entries, so that I maintain accurate and consistent records.

#### Acceptance Criteria

1. WHEN a Time_Entry has an end timestamp, THE Data_Validator SHALL verify that the end timestamp is after the start timestamp
2. THE Data_Validator SHALL verify that all timestamps in a Time_Entry belong to the same calendar day as the Day_File
3. THE Data_Validator SHALL verify that no two Time_Entries have overlapping time ranges
4. THE Data_Validator SHALL verify that at most one Active_Entry exists in a Day_File
5. THE Data_Validator SHALL verify that all Time_Entry IDs are unique within a Day_File

### Requirement 7: Command-Line Interface

**User Story:** As a user, I want a simple command-line interface, so that I can quickly track my time without complex interactions.

#### Acceptance Criteria

1. THE CLI SHALL accept START as a valid command to begin a Work_Session
2. THE CLI SHALL accept STOP as a valid command to end a Work_Session
3. WHEN a command succeeds, THE CLI SHALL display a success message to the user
4. WHEN a command fails, THE CLI SHALL display a descriptive error message to the user
5. IF an invalid command is provided, THEN THE CLI SHALL display usage information

### Requirement 8: Error Handling

**User Story:** As a user, I want clear error messages when something goes wrong, so that I can understand and resolve issues.

#### Acceptance Criteria

1. IF a file I/O error occurs, THEN THE System SHALL display a user-friendly error message without exposing system details
2. IF the JSON data is corrupted, THEN THE System SHALL detect the corruption and inform the user
3. IF a business rule is violated, THEN THE System SHALL prevent the operation and explain the violation
4. THE System SHALL log detailed error information for debugging purposes
5. THE System SHALL maintain data integrity by preventing partial writes during errors

### Requirement 9: JSON Data Format

**User Story:** As a developer, I want time tracking data stored in JSON format, so that it can be easily integrated with other tools and interfaces.

#### Acceptance Criteria

1. THE Storage_Manager SHALL serialize Day_File objects to JSON using Jackson
2. THE Storage_Manager SHALL deserialize JSON files to Day_File objects using Jackson
3. THE System SHALL store Time_Entry objects with fields: id, start, end, totalCalculated, totalUsedForReporting, place, paidOverhours
4. THE System SHALL store Day_File objects with fields: entries, pensum, dayType
5. THE System SHALL use ISO-8601 format for timestamp serialization

### Requirement 10: Data Integrity

**User Story:** As a user, I want my time tracking data to remain consistent and accurate, so that I can trust my work records.

#### Acceptance Criteria

1. THE System SHALL use atomic file writes to prevent data corruption during save operations
2. WHEN loading a Day_File, THE Data_Validator SHALL verify all validation rules before allowing operations
3. THE System SHALL ensure that serializing and deserializing a Day_File produces an equivalent object
4. THE System SHALL prevent modifications that would violate data integrity constraints
5. THE System SHALL maintain referential integrity between Time_Entries and their parent Day_File

### Requirement 11: Performance

**User Story:** As a user, I want the system to respond quickly, so that time tracking doesn't interrupt my workflow.

#### Acceptance Criteria

1. THE System SHALL complete the START command in less than 200 milliseconds
2. THE System SHALL complete the STOP command in less than 200 milliseconds
3. THE Storage_Manager SHALL load a Day_File in less than 50 milliseconds
4. THE Storage_Manager SHALL save a Day_File in less than 50 milliseconds
5. THE System SHALL operate efficiently with years of historical data

### Requirement 12: Workplace Tracking

**User Story:** As a user, I want to record where I'm working, so that I can track time by location.

#### Acceptance Criteria

1. THE System SHALL support OFFICE as a valid workplace value
2. THE System SHALL support HOME as a valid workplace value
3. THE System SHALL support CLIENT as a valid workplace value
4. WHEN creating a Time_Entry, THE System SHALL record the workplace location
5. THE System SHALL persist the workplace value in the JSON file

### Requirement 13: Day Type Classification

**User Story:** As a user, I want to classify days by type, so that I can distinguish between regular workdays and special days.

#### Acceptance Criteria

1. THE System SHALL support REGULAR as a valid day type
2. THE System SHALL support WEEKEND as a valid day type
3. THE System SHALL support VACATION as a valid day type
4. THE System SHALL support SICK_LEAVE as a valid day type
5. THE System SHALL support PUBLIC_HOLIDAY as a valid day type
6. THE System SHALL support TIME_OFF as a valid day type
7. THE System SHALL support VACATION_FOR_SATURDAY_HOLIDAY as a valid day type
8. THE Day_File SHALL store the day type classification

### Requirement 14: Work Pensum

**User Story:** As a user, I want to define my expected work hours per day, so that I can track whether I've met my daily target.

#### Acceptance Criteria

1. THE Day_File SHALL store a pensum value in HH:MM format
2. THE System SHALL validate that pensum matches the pattern ^\d{2}:\d{2}$
3. THE System SHALL persist the pensum value in the JSON file
4. THE System SHALL allow the pensum to be configured per day
5. THE System SHALL support pensum values from 00:00 to 23:59

### Requirement 15: Paid Overhours

**User Story:** As a user, I want to track paid overtime hours, so that I can record compensated extra work time.

#### Acceptance Criteria

1. THE Time_Entry SHALL store a paidOverhours value in HH:MM format
2. THE System SHALL validate that paidOverhours matches the pattern ^\d{2}:\d{2}$
3. THE System SHALL persist the paidOverhours value in the JSON file
4. THE System SHALL allow paidOverhours to be recorded per Time_Entry
5. THE System SHALL support paidOverhours values from 00:00 to 23:59
