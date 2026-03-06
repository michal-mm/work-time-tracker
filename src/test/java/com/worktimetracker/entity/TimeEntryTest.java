package com.worktimetracker.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the TimeEntry entity.
 */
class TimeEntryTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateTimeEntryWithAllFields() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 30);
        
        TimeEntry entry = new TimeEntry(0, start, end, "08:30", "08:30", 
                                       WorkPlace.OFFICE, "00:00");
        
        assertThat(entry.getId()).isEqualTo(0);
        assertThat(entry.getStart()).isEqualTo(start);
        assertThat(entry.getEnd()).isEqualTo(end);
        assertThat(entry.getTotalCalculated()).isEqualTo("08:30");
        assertThat(entry.getTotalUsedForReporting()).isEqualTo("08:30");
        assertThat(entry.getPlace()).isEqualTo(WorkPlace.OFFICE);
        assertThat(entry.getPaidOverhours()).isEqualTo("00:00");
    }

    @Test
    void shouldCreateActiveEntry() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry entry = new TimeEntry(0, start, null, null, null, 
                                       WorkPlace.HOME, null);
        
        assertThat(entry.isActive()).isTrue();
        assertThat(entry.getEnd()).isNull();
    }

    @Test
    void shouldValidatePositiveId() {
        TimeEntry entry = new TimeEntry(5, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidId()).isTrue();
    }

    @Test
    void shouldValidateZeroId() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidId()).isTrue();
    }

    @Test
    void shouldInvalidateNegativeId() {
        TimeEntry entry = new TimeEntry(-1, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidId()).isFalse();
    }

    @Test
    void shouldValidateNonNullStart() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidStart()).isTrue();
    }

    @Test
    void shouldInvalidateNullStart() {
        TimeEntry entry = new TimeEntry(0, null, null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidStart()).isFalse();
    }

    @Test
    void shouldValidateEndAfterStart() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 0);
        
        TimeEntry entry = new TimeEntry(0, start, end, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidEndAfterStart()).isTrue();
    }

    @Test
    void shouldInvalidateEndBeforeStart() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 17, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry entry = new TimeEntry(0, start, end, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidEndAfterStart()).isFalse();
    }

    @Test
    void shouldInvalidateEndEqualToStart() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry entry = new TimeEntry(0, timestamp, timestamp, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidEndAfterStart()).isFalse();
    }

    @Test
    void shouldValidateNullEnd() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidEndAfterStart()).isTrue();
    }

    @Test
    void shouldValidateSameDay() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 23, 59);
        
        TimeEntry entry = new TimeEntry(0, start, end, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidSameDay()).isTrue();
    }

    @Test
    void shouldInvalidateDifferentDays() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 23, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 1, 0);
        
        TimeEntry entry = new TimeEntry(0, start, end, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidSameDay()).isFalse();
    }

    @Test
    void shouldValidateNullEndForSameDay() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.OFFICE, null);
        assertThat(entry.isValidSameDay()).isTrue();
    }

    @Test
    void shouldValidateCorrectTimeFormat() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, 
                                       "08:30", "08:30", WorkPlace.OFFICE, "01:15");
        assertThat(entry.isValidTotalCalculated()).isTrue();
        assertThat(entry.isValidTotalUsedForReporting()).isTrue();
        assertThat(entry.isValidPaidOverhours()).isTrue();
    }

    @Test
    void shouldValidateNullTimeFields() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, 
                                       null, null, WorkPlace.OFFICE, null);
        assertThat(entry.isValidTotalCalculated()).isTrue();
        assertThat(entry.isValidTotalUsedForReporting()).isTrue();
        assertThat(entry.isValidPaidOverhours()).isTrue();
    }

    @Test
    void shouldInvalidateIncorrectTimeFormat() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, 
                                       "8:30", null, WorkPlace.OFFICE, null);
        assertThat(entry.isValidTotalCalculated()).isFalse();
    }

    @Test
    void shouldValidateTimePatternFormat() {
        // Pattern only checks format (two digits : two digits), not semantic validity
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, 
                                       "25:99", null, WorkPlace.OFFICE, null);
        assertThat(entry.isValidTotalCalculated()).isTrue(); // Format is correct even if values are invalid
    }

    @Test
    void shouldInvalidateNonTimeString() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, 
                                       "invalid", null, WorkPlace.OFFICE, null);
        assertThat(entry.isValidTotalCalculated()).isFalse();
    }

    @Test
    void shouldValidateNonNullPlace() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       WorkPlace.CLIENT, null);
        assertThat(entry.isValidPlace()).isTrue();
    }

    @Test
    void shouldInvalidateNullPlace() {
        TimeEntry entry = new TimeEntry(0, LocalDateTime.now(), null, null, null, 
                                       null, null);
        assertThat(entry.isValidPlace()).isFalse();
    }

    @Test
    void shouldValidateCompleteEntry() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 0);
        
        TimeEntry entry = new TimeEntry(0, start, end, "08:00", "08:00", 
                                       WorkPlace.OFFICE, "00:00");
        assertThat(entry.isValid()).isTrue();
    }

    @Test
    void shouldInvalidateEntryWithInvalidFields() {
        TimeEntry entry = new TimeEntry(-1, null, null, "invalid", null, 
                                       null, null);
        assertThat(entry.isValid()).isFalse();
    }

    @Test
    void shouldSerializeToJsonWithSnakeCase() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 0);
        
        TimeEntry entry = new TimeEntry(0, start, end, "08:00", "08:00", 
                                       WorkPlace.OFFICE, "00:30");
        
        String json = objectMapper.writeValueAsString(entry);
        
        assertThat(json).contains("\"total_calculated\"");
        assertThat(json).contains("\"total_used_for_reporting\"");
        assertThat(json).contains("\"paid_overhours\"");
        assertThat(json).contains("\"place\"");
    }

    @Test
    void shouldDeserializeFromJsonWithSnakeCase() throws Exception {
        String json = """
            {
                "id": 0,
                "start": "2024-01-15T09:00:00",
                "end": "2024-01-15T17:00:00",
                "total_calculated": "08:00",
                "total_used_for_reporting": "08:00",
                "place": "OFFICE",
                "paid_overhours": "00:30"
            }
            """;
        
        TimeEntry entry = objectMapper.readValue(json, TimeEntry.class);
        
        assertThat(entry.getId()).isEqualTo(0);
        assertThat(entry.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 15, 9, 0));
        assertThat(entry.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 15, 17, 0));
        assertThat(entry.getTotalCalculated()).isEqualTo("08:00");
        assertThat(entry.getTotalUsedForReporting()).isEqualTo("08:00");
        assertThat(entry.getPlace()).isEqualTo(WorkPlace.OFFICE);
        assertThat(entry.getPaidOverhours()).isEqualTo("00:30");
    }

    @Test
    void shouldRoundTripThroughJson() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 0);
        
        TimeEntry original = new TimeEntry(0, start, end, "08:00", "08:00", 
                                          WorkPlace.HOME, "00:15");
        
        String json = objectMapper.writeValueAsString(original);
        TimeEntry deserialized = objectMapper.readValue(json, TimeEntry.class);
        
        assertThat(deserialized).isEqualTo(original);
    }

    @Test
    void shouldHandleActiveEntryInJson() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry original = new TimeEntry(0, start, null, null, null, 
                                          WorkPlace.CLIENT, null);
        
        String json = objectMapper.writeValueAsString(original);
        TimeEntry deserialized = objectMapper.readValue(json, TimeEntry.class);
        
        assertThat(deserialized.isActive()).isTrue();
        assertThat(deserialized.getEnd()).isNull();
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 17, 0);
        
        TimeEntry entry1 = new TimeEntry(0, start, end, "08:00", "08:00", 
                                        WorkPlace.OFFICE, "00:00");
        TimeEntry entry2 = new TimeEntry(0, start, end, "08:00", "08:00", 
                                        WorkPlace.OFFICE, "00:00");
        
        assertThat(entry1).isEqualTo(entry2);
        assertThat(entry1.hashCode()).isEqualTo(entry2.hashCode());
    }

    @Test
    void shouldImplementEqualsForDifferentEntries() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry entry1 = new TimeEntry(0, start, null, null, null, 
                                        WorkPlace.OFFICE, null);
        TimeEntry entry2 = new TimeEntry(1, start, null, null, null, 
                                        WorkPlace.OFFICE, null);
        
        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void shouldImplementToString() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 9, 0);
        
        TimeEntry entry = new TimeEntry(0, start, null, null, null, 
                                       WorkPlace.OFFICE, null);
        
        String toString = entry.toString();
        
        assertThat(toString).contains("TimeEntry");
        assertThat(toString).contains("id=0");
        assertThat(toString).contains("OFFICE");
    }
}
