package com.worktimetracker.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the type of day for work time tracking.
 * 
 * <p>This enum classifies days into different categories to distinguish between
 * regular workdays and special days such as holidays, vacations, and sick leave.</p>
 * 
 * <p>Supported day types:</p>
 * <ul>
 *   <li>REGULAR - Normal working day</li>
 *   <li>WEEKEND - Saturday or Sunday</li>
 *   <li>VACATION - Planned vacation day</li>
 *   <li>SICK_LEAVE - Day off due to illness</li>
 *   <li>PUBLIC_HOLIDAY - Official public holiday</li>
 *   <li>TIME_OFF - Other time off (personal day, etc.)</li>
 *   <li>VACATION_FOR_SATURDAY_HOLIDAY - Vacation day compensating for a Saturday holiday</li>
 * </ul>
 * 
 * @see com.worktimetracker.entity.DayFile
 */
public enum DayType {
    PUBLIC_HOLIDAY,
    REGULAR,
    SICK_LEAVE,
    TIME_OFF,
    VACATION,
    VACATION_FOR_SATURDAY_HOLIDAY,
    WEEKEND;

    /**
     * Returns the string representation of this day type for JSON serialization.
     * 
     * @return the name of this enum constant
     */
    @JsonValue
    public String toValue() {
        return name();
    }

    /**
     * Creates a DayType from its string representation during JSON deserialization.
     * 
     * @param value the string value to convert
     * @return the corresponding DayType enum constant
     * @throws IllegalArgumentException if the value doesn't match any DayType
     */
    @JsonCreator
    public static DayType fromValue(String value) {
        return valueOf(value);
    }
}
