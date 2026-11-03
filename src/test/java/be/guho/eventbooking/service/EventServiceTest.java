package be.guho.eventbooking.service;

import be.guho.eventbooking.api.exception.EventDateNotInFutureException;
import be.guho.eventbooking.dto.CreateEventRequest;
import be.guho.eventbooking.persistence.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldRejectEventWithDateInThePast() {
        CreateEventRequest request = new CreateEventRequest();
        request.setName("Past event");
        request.setDate(OffsetDateTime.parse("2024-11-07T16:52:40.308Z"));
        request.setCapacity(1);

        assertThatThrownBy(() -> eventService.createEvent(request))
                .isInstanceOf(EventDateNotInFutureException.class)
                .hasMessage("Event date must be in the future");

        verifyNoInteractions(eventRepository);
    }
}
