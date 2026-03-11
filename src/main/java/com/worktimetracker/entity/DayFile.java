package com.worktimetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entity representing all time entries for a single calendar day.
 * 
 * <p>A DayFile aggregates all work sessions (TimeEntry objects) for a specific date,
 * along with metadata such as the expected work hours (pensum) and the type of day
 * (regular, vacation, holiday, etc.).</p>
 * 
 * <p>Key characteristics:</p>
 * <ul>
 *   <li>Contains a list of time entries for the day</li>
 *   <li>Tracks the expected work hours (pensum) in HH:MM format</li>
 *   <li>Classifies the day type (regular, vacation, etc.)</li>
 *   <li>Provides methods to calculate total worked time</li>
 *   <li>Manages entry ID assignment</li>
 *   <li>Identifies active (unclosed) entries</li>
 * </ul>
 * 
 * @see com.worktimetracker.entity.TimeEntry
 * @see com.worktimetracker.entity.DayType
 */
public class DayFile {
    
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{2}:\\d{2}$");
    
    private List<TimeEntry> entries;
    private String pensum;
    
    @JsonProperty("day_type")
    private DayType dayType;
    
    /**
     * Default constructor for Jackson deserialization.
     * Initializes entries as an empty list.
     */
    public DayFile() {
        this.entries = new ArrayList<>();
    }
    
    /**
     * Creates a new DayFile with the specified parameters.
     * 
     * @param entries the list of time entries for this day
     * @param pensum the expected work hours in HH:MM format
     * @param dayType the type of day
     */
    public DayFile(List<TimeEntry> entries, String pensum, DayType dayType) {
        this.entries = entries != null ? entries : new ArrayList<>();
        this.pensum = pensum;
        this.dayType = dayType;
    }
    
    // Getters and Setters
    
    public List<TimeEntry> getEntries() {
        return entries;
    }
    
    public void setEntries(List<TimeEntry> entries) {
        this.entries = entries != null ? entries : new ArrayList<>();
    }
    
    public String getPensum() {
        return pensum;
    }
    
    public void setPensum(String pensum) {
        this.pensum = pensum;
    }
    
    public DayType getDayType() {
        return dayType;
    }
    
    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }
    
    // Derived Methods
    
    /**
     * Gets the next available entry ID for a new time entry.
     * 
     * <p>Entry IDs are sequential starting from 0. This method finds the highest
     * existing ID and returns the next number in sequence.</p>
     * 
     * @return the next available entry ID (0 if no entries exist)
     */
    @JsonIgnore
    public int getNextEntryId() {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }
        return entries.stream()
                .mapToInt(TimeEntry::getId)
                .max()
                .orElse(-1) + 1;
    }
    
    /**
     * Gets the currently active time entry (entry with null end timestamp).
     * 
     * <p>An active entry represents an ongoing work session that has been started
     * but not yet stopped. There should be at most one active entry per day.</p>
     * 
     * @return the active TimeEntry, or null if no active entry exists
     */
    @JsonIgnore
    public TimeEntry getActiveEntry() {
        if (entries == null) {
            return null;
        }
        return entries.stream()
                .filter(TimeEntry::isActive)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Calculates the total worked time across all completed entries.
     * 
     * <p>This method sums the durations of all time entries that have both start
     * and end timestamps. Active entries (with null end) are not included in the
     * calculation.</p>
     * 
     * @return the total worked time as a Duration, or Duration.ZERO if no completed entries
     */
    @JsonIgnore
    public Duration getTotalWorkedTime() {
        if (entries == null || entries.isEmpty()) {
            return Duration.ZERO;
        }
        
        return entries.stream()
                .filter(entry -> entry.getStart() != null && entry.getEnd() != null)
                .map(entry -> Duration.between(entry.getStart(), entry.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);
    }
    
    // Validation Methods
    
    /**
     * Validates that the pensum matches the HH:MM pattern.
     * 
     * @return true if pensum is null or matches the pattern, false otherwise
     */
    @JsonIgnore
    public boolean isValidPensum() {
        return pensum == null || TIME_PATTERN.matcher(pensum).matches();
    }
    
    /**
     * Validates that the day type is not null.
     * 
     * @return true if dayType is not null, false otherwise
     */
    @JsonIgnore
    public boolean isValidDayType() {
        return dayType != null;
    }
    
    /**
     * Validates that the entries list is not null.
     * 
     * @return true if entries is not null, false otherwise
     */
    @JsonIgnore
    public boolean isValidEntries() {
        return entries != null;
    }
    
    /**
     * Validates that all entry IDs are unique within this day file.
     * 
     * @return true if all IDs are unique, false otherwise
     */
    @JsonIgnore
    public boolean hasUniqueEntryIds() {
        if (entries == null || entries.isEmpty()) {
            return true;
        }
        long uniqueCount = entries.stream()
                .mapToInt(TimeEntry::getId)
                .distinct()
                .count();
        return uniqueCount == entries.size();
    }
    
    /**
     * Validates that at most one active entry exists.
     * 
     * @return true if there is at most one active entry, false otherwise
     */
    @JsonIgnore
    public boolean hasAtMostOneActiveEntry() {
        if (entries == null) {
            return true;
        }
        long activeCount = entries.stream()
                .filter(TimeEntry::isActive)
                .count();
        return activeCount <= 1;
    }
    
    /**
     * Performs comprehensive validation of all fields.
     * 
     * @return true if all validation rules pass, false otherwise
     */
    @JsonIgnore
    public boolean isValid() {
        return isValidPensum()
            && isValidDayType()
            && isValidEntries()
            && hasUniqueEntryIds()
            && hasAtMostOneActiveEntry();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayFile dayFile = (DayFile) o;
        return Objects.equals(entries, dayFile.entries)
            && Objects.equals(pensum, dayFile.pensum)
            && dayType == dayFile.dayType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entries, pensum, dayType);
    }
    
    @Override
    public String toString() {
        return "DayFile{" +
                "entries=" + entries +
                ", pensum='" + pensum + '\'' +
                ", dayType=" + dayType +
                '}';
    }
}
