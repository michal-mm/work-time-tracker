package com.worktimetracker.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the WorkPlace enumeration.
 */
class WorkPlaceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldHaveThreeValues() {
        assertThat(WorkPlace.values()).hasSize(3);
    }

    @Test
    void shouldContainOfficeValue() {
        assertThat(WorkPlace.valueOf("OFFICE")).isEqualTo(WorkPlace.OFFICE);
    }

    @Test
    void shouldContainHomeValue() {
        assertThat(WorkPlace.valueOf("HOME")).isEqualTo(WorkPlace.HOME);
    }

    @Test
    void shouldContainClientValue() {
        assertThat(WorkPlace.valueOf("CLIENT")).isEqualTo(WorkPlace.CLIENT);
    }

    @Test
    void shouldSerializeToJson() throws Exception {
        String json = objectMapper.writeValueAsString(WorkPlace.OFFICE);
        assertThat(json).isEqualTo("\"OFFICE\"");
    }

    @Test
    void shouldDeserializeFromJson() throws Exception {
        WorkPlace workPlace = objectMapper.readValue("\"HOME\"", WorkPlace.class);
        assertThat(workPlace).isEqualTo(WorkPlace.HOME);
    }

    @Test
    void shouldRoundTripThroughJson() throws Exception {
        for (WorkPlace workPlace : WorkPlace.values()) {
            String json = objectMapper.writeValueAsString(workPlace);
            WorkPlace deserialized = objectMapper.readValue(json, WorkPlace.class);
            assertThat(deserialized).isEqualTo(workPlace);
        }
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThatThrownBy(() -> WorkPlace.valueOf("INVALID"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionForInvalidJsonValue() {
        assertThatThrownBy(() -> objectMapper.readValue("\"INVALID\"", WorkPlace.class))
            .isInstanceOf(Exception.class);
    }
}
