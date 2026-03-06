package com.worktimetracker.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the workplace location for a work time entry.
 * 
 * <p>This enum tracks where the work was performed, enabling location-based
 * time tracking and reporting.</p>
 * 
 * <p>Supported workplace locations:</p>
 * <ul>
 *   <li>OFFICE - Work performed at the company office</li>
 *   <li>HOME - Work performed from home (remote work)</li>
 *   <li>CLIENT - Work performed at a client's location</li>
 * </ul>
 * 
 * @see com.worktimetracker.entity.TimeEntry
 */
public enum WorkPlace {
    OFFICE,
    HOME,
    CLIENT;

    /**
     * Returns the string representation of this workplace for JSON serialization.
     * 
     * @return the name of this enum constant
     */
    @JsonValue
    public String toValue() {
        return name();
    }

    /**
     * Creates a WorkPlace from its string representation during JSON deserialization.
     * 
     * @param value the string value to convert
     * @return the corresponding WorkPlace enum constant
     * @throws IllegalArgumentException if the value doesn't match any WorkPlace
     */
    @JsonCreator
    public static WorkPlace fromValue(String value) {
        return valueOf(value);
    }
}
