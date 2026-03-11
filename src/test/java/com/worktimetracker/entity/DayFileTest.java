package com.worktimetracker.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the DayFile entity.
 */
class DayFileTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateDayFileWithAllFields() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getEntries()).hasSize(1);
        assertThat(dayFile.getPensum()).isEqualTo("08:00");
        assertThat(dayFile.getDayType()).isEqualTo(DayType.REGULAR);
    }

    @Test
    void shouldCreateEmptyDayFile() {
        DayFile dayFile = new DayFile();
        
        assertThat(dayFile.getEntries()).isNotNull();
        assertThat(dayFile.getEntries()).isEmpty();
    }

    @Test
    void shouldHandleNullEntriesInConstructor() {
        DayFile dayFile = new DayFile(null, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getEntries()).isNotNull();
        assertThat(dayFile.getEntries()).isEmpty();
    }

    @Test
    void shouldGetNextEntryIdForEmptyList() {
        DayFile dayFile = new DayFile();
        
        assertThat(dayFile.getNextEntryId()).isEqualTo(0);
    }

    @Test
    void shouldGetNextEntryIdForSingleEntry() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getNextEntryId()).isEqualTo(1);
    }

    @Test
    void shouldGetNextEntryIdForMultipleEntries() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        entries.add(createTimeEntry(2, 18, 0, 20, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getNextEntryId()).isEqualTo(3);
    }

    @Test
    void shouldGetNextEntryIdWithNonSequentialIds() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(5, 13, 0, 17, 0));
        entries.add(createTimeEntry(2, 18, 0, 20, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getNextEntryId()).isEqualTo(6);
    }

    @Test
    void shouldReturnNullWhenNoActiveEntry() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getActiveEntry()).isNull();
    }

    @Test
    void shouldReturnActiveEntry() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        TimeEntry activeEntry = createActiveEntry(1, 13, 0);
        entries.add(activeEntry);
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getActiveEntry()).isEqualTo(activeEntry);
        assertThat(dayFile.getActiveEntry().isActive()).isTrue();
    }

    @Test
    void shouldReturnNullActiveEntryForEmptyList() {
        DayFile dayFile = new DayFile();
        
        assertThat(dayFile.getActiveEntry()).isNull();
    }

    @Test
    void shouldCalculateTotalWorkedTimeForEmptyList() {
        DayFile dayFile = new DayFile();
        
        assertThat(dayFile.getTotalWorkedTime()).isEqualTo(Duration.ZERO);
    }

    @Test
    void shouldCalculateTotalWorkedTimeForSingleEntry() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0)); // 3 hours
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getTotalWorkedTime()).isEqualTo(Duration.ofHours(3));
    }

    @Test
    void shouldCalculateTotalWorkedTimeForMultipleEntries() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));   // 3 hours
        entries.add(createTimeEntry(1, 13, 0, 17, 30)); // 4.5 hours
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getTotalWorkedTime()).isEqualTo(Duration.ofMinutes(450)); // 7.5 hours
    }

    @Test
    void shouldExcludeActiveEntryFromTotalWorkedTime() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));   // 3 hours
        entries.add(createActiveEntry(1, 13, 0));       // Active, should be excluded
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.getTotalWorkedTime()).isEqualTo(Duration.ofHours(3));
    }

    @Test
    void shouldValidateCorrectPensumFormat() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "08:00", DayType.REGULAR);
        
        assertThat(dayFile.isValidPensum()).isTrue();
    }

    @Test
    void shouldValidateNullPensum() {
        DayFile dayFile = new DayFile(new ArrayList<>(), null, DayType.REGULAR);
        
        assertThat(dayFile.isValidPensum()).isTrue();
    }

    @Test
    void shouldInvalidateIncorrectPensumFormat() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "8:00", DayType.REGULAR);
        
        assertThat(dayFile.isValidPensum()).isFalse();
    }

    @Test
    void shouldInvalidateNonTimePensum() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "invalid", DayType.REGULAR);
        
        assertThat(dayFile.isValidPensum()).isFalse();
    }

    @Test
    void shouldValidateNonNullDayType() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "08:00", DayType.WEEKEND);
        
        assertThat(dayFile.isValidDayType()).isTrue();
    }

    @Test
    void shouldInvalidateNullDayType() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "08:00", null);
        
        assertThat(dayFile.isValidDayType()).isFalse();
    }

    @Test
    void shouldValidateNonNullEntries() {
        DayFile dayFile = new DayFile(new ArrayList<>(), "08:00", DayType.REGULAR);
        
        assertThat(dayFile.isValidEntries()).isTrue();
    }

    @Test
    void shouldValidateUniqueEntryIds() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        entries.add(createTimeEntry(2, 18, 0, 20, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.hasUniqueEntryIds()).isTrue();
    }

    @Test
    void shouldInvalidateDuplicateEntryIds() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        entries.add(createTimeEntry(1, 18, 0, 20, 0)); // Duplicate ID
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.hasUniqueEntryIds()).isFalse();
    }

    @Test
    void shouldValidateEmptyListForUniqueIds() {
        DayFile dayFile = new DayFile();
        
        assertThat(dayFile.hasUniqueEntryIds()).isTrue();
    }

    @Test
    void shouldValidateAtMostOneActiveEntry() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createActiveEntry(1, 13, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.hasAtMostOneActiveEntry()).isTrue();
    }

    @Test
    void shouldValidateNoActiveEntries() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.hasAtMostOneActiveEntry()).isTrue();
    }

    @Test
    void shouldInvalidateMultipleActiveEntries() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createActiveEntry(0, 9, 0));
        entries.add(createActiveEntry(1, 13, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.hasAtMostOneActiveEntry()).isFalse();
    }

    @Test
    void shouldValidateCompleteDayFile() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        assertThat(dayFile.isValid()).isTrue();
    }

    @Test
    void shouldInvalidateDayFileWithInvalidFields() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(0, 13, 0, 17, 0)); // Duplicate ID
        
        DayFile dayFile = new DayFile(entries, "invalid", null);
        
        assertThat(dayFile.isValid()).isFalse();
    }

    @Test
    void shouldSerializeToJsonWithSnakeCase() throws Exception {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        String json = objectMapper.writeValueAsString(dayFile);
        
        assertThat(json).contains("\"day_type\"");
        assertThat(json).contains("\"pensum\"");
        assertThat(json).contains("\"entries\"");
    }

    @Test
    void shouldDeserializeFromJsonWithSnakeCase() throws Exception {
        String json = """
            {
                "entries": [
                    {
                        "id": 0,
                        "start": "2024-01-15T09:00:00",
                        "end": "2024-01-15T12:00:00",
                        "total_calculated": "03:00",
                        "total_used_for_reporting": "03:00",
                        "place": "OFFICE",
                        "paid_overhours": "00:00"
                    }
                ],
                "pensum": "08:00",
                "day_type": "REGULAR"
            }
            """;
        
        DayFile dayFile = objectMapper.readValue(json, DayFile.class);
        
        assertThat(dayFile.getEntries()).hasSize(1);
        assertThat(dayFile.getPensum()).isEqualTo("08:00");
        assertThat(dayFile.getDayType()).isEqualTo(DayType.REGULAR);
    }

    @Test
    void shouldRoundTripThroughJson() throws Exception {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        entries.add(createTimeEntry(1, 13, 0, 17, 0));
        
        DayFile original = new DayFile(entries, "08:00", DayType.VACATION);
        
        String json = objectMapper.writeValueAsString(original);
        DayFile deserialized = objectMapper.readValue(json, DayFile.class);
        
        assertThat(deserialized).isEqualTo(original);
    }

    @Test
    void shouldHandleAllDayTypes() throws Exception {
        for (DayType dayType : DayType.values()) {
            DayFile dayFile = new DayFile(new ArrayList<>(), "08:00", dayType);
            
            String json = objectMapper.writeValueAsString(dayFile);
            DayFile deserialized = objectMapper.readValue(json, DayFile.class);
            
            assertThat(deserialized.getDayType()).isEqualTo(dayType);
        }
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        List<TimeEntry> entries1 = new ArrayList<>();
        entries1.add(createTimeEntry(0, 9, 0, 12, 0));
        
        List<TimeEntry> entries2 = new ArrayList<>();
        entries2.add(createTimeEntry(0, 9, 0, 12, 0));
        
        DayFile dayFile1 = new DayFile(entries1, "08:00", DayType.REGULAR);
        DayFile dayFile2 = new DayFile(entries2, "08:00", DayType.REGULAR);
        
        assertThat(dayFile1).isEqualTo(dayFile2);
        assertThat(dayFile1.hashCode()).isEqualTo(dayFile2.hashCode());
    }

    @Test
    void shouldImplementEqualsForDifferentDayFiles() {
        DayFile dayFile1 = new DayFile(new ArrayList<>(), "08:00", DayType.REGULAR);
        DayFile dayFile2 = new DayFile(new ArrayList<>(), "08:00", DayType.WEEKEND);
        
        assertThat(dayFile1).isNotEqualTo(dayFile2);
    }

    @Test
    void shouldImplementToString() {
        List<TimeEntry> entries = new ArrayList<>();
        entries.add(createTimeEntry(0, 9, 0, 12, 0));
        
        DayFile dayFile = new DayFile(entries, "08:00", DayType.REGULAR);
        
        String toString = dayFile.toString();
        
        assertThat(toString).contains("DayFile");
        assertThat(toString).contains("08:00");
        assertThat(toString).contains("REGULAR");
    }

    // Helper methods

    private TimeEntry createTimeEntry(int id, int startHour, int startMinute, 
                                     int endHour, int endMinute) {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, endHour, endMinute);
        Duration duration = Duration.between(start, end);
        String durationStr = String.format("%02d:%02d", 
                                          duration.toHours(), 
                                          duration.toMinutesPart());
        
        return new TimeEntry(id, start, end, durationStr, durationStr, 
                           WorkPlace.OFFICE, "00:00");
    }

    private TimeEntry createActiveEntry(int id, int startHour, int startMinute) {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, startHour, startMinute);
        return new TimeEntry(id, start, null, null, null, WorkPlace.OFFICE, null);
    }
}
