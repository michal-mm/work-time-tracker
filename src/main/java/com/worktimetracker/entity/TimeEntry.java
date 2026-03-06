package com.worktimetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entity representing a single work time entry with start and optional end timestamp.
 * 
 * <p>A TimeEntry records a work session, tracking when work started, when it ended
 * (if completed), the calculated duration, and additional metadata such as workplace
 * location and paid overtime hours.</p>
 * 
 * <p>Key characteristics:</p>
 * <ul>
 *   <li>Each entry has a unique ID within its day file</li>
 *   <li>Active entries have a null end timestamp</li>
 *   <li>Completed entries have both start and end timestamps</li>
 *   <li>Durations are stored in HH:MM format</li>
 *   <li>All timestamps must be on the same calendar day</li>
 * </ul>
 * 
 * @see com.worktimetracker.entity.WorkPlace
 * @see com.worktimetracker.entity.DayFile
 */
public class TimeEntry {
    
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{2}:\\d{2}$");
    
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    
    @JsonProperty("total_calculated")
    private String totalCalculated;
    
    @JsonProperty("total_used_for_reporting")
    private String totalUsedForReporting;
    
    private WorkPlace place;
    
    @JsonProperty("paid_overhours")
    private String paidOverhours;
    
    /**
     * Default constructor for Jackson deserialization.
     */
    public TimeEntry() {
    }
    
    /**
     * Creates a new TimeEntry with the specified parameters.
     * 
     * @param id the unique identifier for this entry within its day file
     * @param start the start timestamp of the work session
     * @param end the end timestamp of the work session (null if active)
     * @param totalCalculated the calculated duration in HH:MM format
     * @param totalUsedForReporting the duration used for reporting in HH:MM format
     * @param place the workplace location
     * @param paidOverhours the paid overtime hours in HH:MM format
     */
    public TimeEntry(int id, LocalDateTime start, LocalDateTime end, 
                     String totalCalculated, String totalUsedForReporting,
                     WorkPlace place, String paidOverhours) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.totalCalculated = totalCalculated;
        this.totalUsedForReporting = totalUsedForReporting;
        this.place = place;
        this.paidOverhours = paidOverhours;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDateTime getStart() {
        return start;
    }
    
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    
    public LocalDateTime getEnd() {
        return end;
    }
    
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    
    public String getTotalCalculated() {
        return totalCalculated;
    }
    
    public void setTotalCalculated(String totalCalculated) {
        this.totalCalculated = totalCalculated;
    }
    
    public String getTotalUsedForReporting() {
        return totalUsedForReporting;
    }
    
    public void setTotalUsedForReporting(String totalUsedForReporting) {
        this.totalUsedForReporting = totalUsedForReporting;
    }
    
    public WorkPlace getPlace() {
        return place;
    }
    
    public void setPlace(WorkPlace place) {
        this.place = place;
    }
    
    public String getPaidOverhours() {
        return paidOverhours;
    }
    
    public void setPaidOverhours(String paidOverhours) {
        this.paidOverhours = paidOverhours;
    }
    
    // Validation Methods
    
    /**
     * Validates that the ID is non-negative.
     * 
     * @return true if the ID is >= 0, false otherwise
     */
    @JsonIgnore
    public boolean isValidId() {
        return id >= 0;
    }
    
    /**
     * Validates that the start timestamp is non-null.
     * 
     * @return true if start is not null, false otherwise
     */
    @JsonIgnore
    public boolean isValidStart() {
        return start != null;
    }
    
    /**
     * Validates that the end timestamp is after the start timestamp.
     * Only applicable when end is not null.
     * 
     * @return true if end is null or end is after start, false otherwise
     */
    @JsonIgnore
    public boolean isValidEndAfterStart() {
        return end == null || end.isAfter(start);
    }
    
    /**
     * Validates that start and end timestamps are on the same calendar day.
     * Only applicable when end is not null.
     * 
     * @return true if end is null or both timestamps are on the same day, false otherwise
     */
    @JsonIgnore
    public boolean isValidSameDay() {
        if (end == null || start == null) {
            return true;
        }
        return start.toLocalDate().equals(end.toLocalDate());
    }
    
    /**
     * Validates that a time string matches the HH:MM pattern.
     * 
     * @param timeString the time string to validate
     * @return true if the string matches the pattern, false otherwise
     */
    private boolean isValidTimeFormat(String timeString) {
        return timeString != null && TIME_PATTERN.matcher(timeString).matches();
    }
    
    /**
     * Validates that totalCalculated matches the HH:MM pattern.
     * 
     * @return true if totalCalculated is null or matches the pattern, false otherwise
     */
    @JsonIgnore
    public boolean isValidTotalCalculated() {
        return totalCalculated == null || isValidTimeFormat(totalCalculated);
    }
    
    /**
     * Validates that totalUsedForReporting matches the HH:MM pattern.
     * 
     * @return true if totalUsedForReporting is null or matches the pattern, false otherwise
     */
    @JsonIgnore
    public boolean isValidTotalUsedForReporting() {
        return totalUsedForReporting == null || isValidTimeFormat(totalUsedForReporting);
    }
    
    /**
     * Validates that paidOverhours matches the HH:MM pattern.
     * 
     * @return true if paidOverhours is null or matches the pattern, false otherwise
     */
    @JsonIgnore
    public boolean isValidPaidOverhours() {
        return paidOverhours == null || isValidTimeFormat(paidOverhours);
    }
    
    /**
     * Validates that the place is not null.
     * 
     * @return true if place is not null, false otherwise
     */
    @JsonIgnore
    public boolean isValidPlace() {
        return place != null;
    }
    
    /**
     * Checks if this entry is active (end timestamp is null).
     * 
     * @return true if this entry is active, false otherwise
     */
    @JsonIgnore
    public boolean isActive() {
        return end == null;
    }
    
    /**
     * Performs comprehensive validation of all fields.
     * 
     * @return true if all validation rules pass, false otherwise
     */
    @JsonIgnore
    public boolean isValid() {
        return isValidId()
            && isValidStart()
            && isValidEndAfterStart()
            && isValidSameDay()
            && isValidTotalCalculated()
            && isValidTotalUsedForReporting()
            && isValidPlace()
            && isValidPaidOverhours();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return id == timeEntry.id
            && Objects.equals(start, timeEntry.start)
            && Objects.equals(end, timeEntry.end)
            && Objects.equals(totalCalculated, timeEntry.totalCalculated)
            && Objects.equals(totalUsedForReporting, timeEntry.totalUsedForReporting)
            && place == timeEntry.place
            && Objects.equals(paidOverhours, timeEntry.paidOverhours);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, totalCalculated, 
                          totalUsedForReporting, place, paidOverhours);
    }
    
    @Override
    public String toString() {
        return "TimeEntry{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", totalCalculated='" + totalCalculated + '\'' +
                ", totalUsedForReporting='" + totalUsedForReporting + '\'' +
                ", place=" + place +
                ", paidOverhours='" + paidOverhours + '\'' +
                '}';
    }
}
