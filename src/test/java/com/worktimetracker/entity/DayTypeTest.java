package com.worktimetracker.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the DayType enumeration.
 */
class DayTypeTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldHaveSevenValues() {
        assertThat(DayType.values()).hasSize(7);
    }

    @Test
    void shouldContainPublicHolidayValue() {
        assertThat(DayType.valueOf("PUBLIC_HOLIDAY")).isEqualTo(DayType.PUBLIC_HOLIDAY);
    }

    @Test
    void shouldContainRegularValue() {
        assertThat(DayType.valueOf("REGULAR")).isEqualTo(DayType.REGULAR);
    }

    @Test
    void shouldContainSickLeaveValue() {
        assertThat(DayType.valueOf("SICK_LEAVE")).isEqualTo(DayType.SICK_LEAVE);
    }

    @Test
    void shouldContainTimeOffValue() {
        assertThat(DayType.valueOf("TIME_OFF")).isEqualTo(DayType.TIME_OFF);
    }

    @Test
    void shouldContainVacationValue() {
        assertThat(DayType.valueOf("VACATION")).isEqualTo(DayType.VACATION);
    }

    @Test
    void shouldContainVacationForSaturdayHolidayValue() {
        assertThat(DayType.valueOf("VACATION_FOR_SATURDAY_HOLIDAY"))
            .isEqualTo(DayType.VACATION_FOR_SATURDAY_HOLIDAY);
    }

    @Test
    void shouldContainWeekendValue() {
        assertThat(DayType.valueOf("WEEKEND")).isEqualTo(DayType.WEEKEND);
    }

    @Test
    void shouldSerializeToJson() throws Exception {
        String json = objectMapper.writeValueAsString(DayType.REGULAR);
        assertThat(json).isEqualTo("\"REGULAR\"");
    }

    @Test
    void shouldDeserializeFromJson() throws Exception {
        DayType dayType = objectMapper.readValue("\"VACATION\"", DayType.class);
        assertThat(dayType).isEqualTo(DayType.VACATION);
    }

    @Test
    void shouldRoundTripThroughJson() throws Exception {
        for (DayType dayType : DayType.values()) {
            String json = objectMapper.writeValueAsString(dayType);
            DayType deserialized = objectMapper.readValue(json, DayType.class);
            assertThat(deserialized).isEqualTo(dayType);
        }
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThatThrownBy(() -> DayType.valueOf("INVALID"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionForInvalidJsonValue() {
        assertThatThrownBy(() -> objectMapper.readValue("\"INVALID\"", DayType.class))
            .isInstanceOf(Exception.class);
    }
}
