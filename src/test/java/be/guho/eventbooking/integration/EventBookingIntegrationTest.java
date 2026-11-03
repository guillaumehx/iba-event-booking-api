package be.guho.eventbooking.integration;

import be.guho.eventbooking.dto.CreateBookingRequest;
import be.guho.eventbooking.dto.CreateEventRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class EventBookingIntegrationTest extends IntegrationTest {

    private static final String USER = "usr-tst";
    private static final String EVENT = "evt-tst";

    @Test
    void shouldCreateCreateEvent() throws Exception {
        CreateEventRequest eventRequest = new CreateEventRequest();
        eventRequest.setName("Spring Boot Workshop");
        eventRequest.setDate(OffsetDateTime.now().plusDays(5));
        eventRequest.setCapacity(50);

        mockMvc.perform(post("/api/events")
                        .header("X-User-Id", "usr-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldBookSeatsSuccessfully() throws Exception {
        int bookedBefore = bookedSeats(EVENT);
        final int numberOfSeatsToBook = 3;

        bookSeats(EVENT, numberOfSeatsToBook, USER)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(EVENT))
                .andExpect(jsonPath("$.userId").value(USER))
                .andExpect(jsonPath("$.seats").value(numberOfSeatsToBook));

        mockMvc.perform(get("/api/bookings").header("X-User-Id", USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].eventId").value(EVENT))
                .andExpect(jsonPath("$[0].seats").value(numberOfSeatsToBook));

        assertThat(bookedSeats(EVENT)).isEqualTo(bookedBefore + numberOfSeatsToBook);
    }

    @Test
    void bookSeats_shouldRejectNonPositiveSeats() throws Exception {
        int bookedBefore = bookedSeats(EVENT);

        mockMvc.perform(post("/api/bookings")
                .header("X-User-Id", USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateBookingRequest(EVENT, -1))))
                .andExpect(status().isBadRequest());

        assertThat(eventRepository.findById(EVENT).orElseThrow().getBookedSeats())
                .isEqualTo(bookedBefore);
    }

    @Test
    void shouldRejectBookingWhenCapacityReached() throws Exception {
        int bookedBefore = bookedSeats(EVENT);
        bookSeats(EVENT, 101, USER).andExpect(status().isUnprocessableContent());
        assertThat(bookedSeats(EVENT)).isEqualTo(bookedBefore);
    }
}