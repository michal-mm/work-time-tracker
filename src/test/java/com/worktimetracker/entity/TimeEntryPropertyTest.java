package com.worktimetracker.entity;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Property-based tests for the TimeEntry entity using JUnit QuickCheck.
 */
@RunWith(JUnitQuickcheck.class)
public class TimeEntryPropertyTest {

    /**
     * Property-Based Test: End Timestamp After Start
     * 
     * **Validates: Requirements 6.1**
     * 
     * Property: For any TimeEntry with a non-null end timestamp, the end timestamp
     * must be strictly after the start timestamp.
     * 
     * This property test generates random TimeEntry objects with various combinations
     * of start and end timestamps, and verifies that the validation correctly identifies
     * when end is after start.
     */
    @Property(trials = 100)
    public void endTimestampMustBeAfterStart(@From(TimeEntryGenerator.class) TimeEntry entry) {
        // Property: If end is not null, then isValidEndAfterStart() should return true
        // if and only if end is strictly after start
        
        if (entry.getEnd() == null) {
            // Active entries (null end) should always pass validation
            assertThat(entry.isValidEndAfterStart())
                .as("Active entry (null end) should be valid")
                .isTrue();
        } else {
            // Completed entries must have end after start
            boolean endIsAfterStart = entry.getEnd().isAfter(entry.getStart());
            
            assertThat(entry.isValidEndAfterStart())
                .as("Entry with end=%s and start=%s should be valid=%s", 
                    entry.getEnd(), entry.getStart(), endIsAfterStart)
                .isEqualTo(endIsAfterStart);
        }
    }
}
