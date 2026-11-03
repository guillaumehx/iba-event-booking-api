package be.guho.eventbooking.integration;

import be.guho.eventbooking.dto.CreateBookingRequest;
import be.guho.eventbooking.persistence.repository.BookingRepository;
import be.guho.eventbooking.persistence.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
public abstract class IntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected BookingRepository bookingRepository;

    @BeforeEach
    void setUpBase() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected int makeConcurrentBooking(String eventId, String userId, CountDownLatch ready, CountDownLatch start) throws Exception {
        ready.countDown();
        if (!start.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Timed out waiting to start concurrent booking");
        }
        return bookSeats(eventId, 1, userId).andReturn().getResponse().getStatus();
    }

    protected ResultActions bookSeats(String eventId, int seats, String userId) throws Exception {
        return mockMvc.perform(post("/api/bookings")
                .header("X-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateBookingRequest(eventId, seats))));
    }

    protected int bookedSeats(String eventId) {
        return eventRepository.findById(eventId).orElseThrow().getBookedSeats();
    }
}
