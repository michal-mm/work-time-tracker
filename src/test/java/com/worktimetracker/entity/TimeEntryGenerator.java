package com.worktimetracker.entity;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.time.LocalDateTime;

/**
 * Custom generator for TimeEntry objects used in property-based testing.
 * Generates TimeEntry instances with various combinations of start/end timestamps.
 */
public class TimeEntryGenerator extends Generator<TimeEntry> {

    public TimeEntryGenerator() {
        super(TimeEntry.class);
    }

    @Override
    public TimeEntry generate(SourceOfRandomness random, GenerationStatus status) {
        // Generate a random ID (0-999)
        int id = random.nextInt(1000);
        
        // Generate a random start timestamp within a reasonable range
        // Year: 2020-2030, Month: 1-12, Day: 1-28 (to avoid invalid dates)
        int year = 2020 + random.nextInt(11); // 2020-2030
        int month = 1 + random.nextInt(12); // 1-12
        int day = 1 + random.nextInt(28); // 1-28
        int startHour = random.nextInt(24); // 0-23
        int startMinute = random.nextInt(60); // 0-59
        
        LocalDateTime start = LocalDateTime.of(year, month, day, startHour, startMinute);
        
        // Randomly decide if this entry should have an end timestamp
        // 70% chance of having an end timestamp, 30% chance of being active (null end)
        LocalDateTime end = null;
        if (random.nextDouble() < 0.7) {
            // Generate end timestamp
            // 50% chance it's after start (valid), 50% chance it's before/equal (invalid)
            if (random.nextBoolean()) {
                // Valid case: end is after start (1 minute to 12 hours later, same day)
                int minutesToAdd = 1 + random.nextInt(60 * 12); // 1 to 720 minutes
                end = start.plusMinutes(minutesToAdd);
                
                // Ensure it's still the same day
                if (!end.toLocalDate().equals(start.toLocalDate())) {
                    // If it crosses to next day, cap it at 23:59 of the same day
                    end = start.toLocalDate().atTime(23, 59);
                }
            } else {
                // Invalid case: end is before or equal to start
                if (random.nextBoolean()) {
                    // End before start
                    int minutesToSubtract = 1 + random.nextInt(60 * 12);
                    end = start.minusMinutes(minutesToSubtract);
                } else {
                    // End equal to start
                    end = start;
                }
            }
        }
        
        // Generate time strings in HH:MM format (or null)
        String totalCalculated = random.nextBoolean() ? 
            String.format("%02d:%02d", random.nextInt(24), random.nextInt(60)) : null;
        String totalUsedForReporting = random.nextBoolean() ? 
            String.format("%02d:%02d", random.nextInt(24), random.nextInt(60)) : null;
        String paidOverhours = random.nextBoolean() ? 
            String.format("%02d:%02d", random.nextInt(24), random.nextInt(60)) : null;
        
        // Random workplace
        WorkPlace[] places = WorkPlace.values();
        int placeIndex = random.nextInt(places.length);
        WorkPlace place = places[placeIndex];
        
        return new TimeEntry(id, start, end, totalCalculated, totalUsedForReporting, 
                           place, paidOverhours);
    }
}
