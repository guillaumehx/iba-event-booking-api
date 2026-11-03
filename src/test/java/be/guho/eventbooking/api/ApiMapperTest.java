package be.guho.eventbooking.api;

import be.guho.eventbooking.dto.BookingResponse;
import be.guho.eventbooking.dto.BookingStatus;
import be.guho.eventbooking.dto.EventResponse;
import be.guho.eventbooking.persistence.entity.BookingEntity;
import be.guho.eventbooking.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiMapperTest {

    private final ApiMapper mapper = new ApiMapperImpl();

    @Test
    void toBookingResponse_shouldMapAllFields() {
        Instant createdAt = Instant.parse("2026-09-01T10:15:00Z");
        BookingEntity entity = mock(BookingEntity.class);
        when(entity.getId()).thenReturn("bkg-001");
        when(entity.getEventId()).thenReturn("evt-01");
        when(entity.getUserId()).thenReturn("usr-01");
        when(entity.getSeats()).thenReturn(2);
        when(entity.getStatus()).thenReturn(BookingStatus.CONFIRMED);
        when(entity.getCreatedAt()).thenReturn(createdAt);

        BookingResponse response = mapper.toBookingResponse(entity);

        assertThat(response.getId()).isEqualTo("bkg-001");
        assertThat(response.getEventId()).isEqualTo("evt-01");
        assertThat(response.getUserId()).isEqualTo("usr-01");
        assertThat(response.getSeats()).isEqualTo(2);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.getCreatedAt()).isEqualTo(OffsetDateTime.parse("2026-09-01T10:15:00Z"));
        assertThat(response.getCreatedAt().getOffset()).isEqualTo(ZoneOffset.UTC);
    }

    @Test
    void toBookingResponse_shouldReturnNullWhenEntityIsNull() {
        assertThat(mapper.toBookingResponse(null)).isNull();
    }

    @Test
    void toBookingResponseList_shouldMapEveryElement() {
        BookingEntity first = mock(BookingEntity.class);
        when(first.getId()).thenReturn("bkg-001");
        BookingEntity second = mock(BookingEntity.class);
        when(second.getId()).thenReturn("bkg-002");

        List<BookingResponse> responses = mapper.toBookingResponseList(List.of(first, second));

        assertThat(responses).extracting(BookingResponse::getId)
                .containsExactly("bkg-001", "bkg-002");
    }

    @Test
    void toBookingResponseList_shouldReturnEmptyListForEmptyInput() {
        assertThat(mapper.toBookingResponseList(List.of())).isEmpty();
    }

    @Test
    void toEventResponse_shouldMapMainFields() {
        EventEntity entity = mock(EventEntity.class);
        when(entity.getId()).thenReturn("evt-01");
        when(entity.getName()).thenReturn("Kotlin Conf Afterparty");
        when(entity.getCapacity()).thenReturn(200);

        EventResponse response = mapper.toEventResponse(entity);

        assertThat(response.getId()).isEqualTo("evt-01");
        assertThat(response.getName()).isEqualTo("Kotlin Conf Afterparty");
        assertThat(response.getCapacity()).isEqualTo(200);
    }

    @Test
    void toEventResponseList_shouldMapEveryElement() {
        EventEntity entity = mock(EventEntity.class);
        when(entity.getId()).thenReturn("evt-01");

        List<EventResponse> responses = mapper.toEventResponseList(List.of(entity));

        assertThat(responses).extracting(EventResponse::getId).containsExactly("evt-01");
    }

    @Test
    void shouldConvertInstantToUtcOffsetDateTime() {
        Instant instant = Instant.parse("2027-03-12T19:00:00Z");

        assertThat(mapper.map(instant))
                .isEqualTo(OffsetDateTime.of(2027, 3, 12, 19, 0, 0, 0, ZoneOffset.UTC));
    }

}