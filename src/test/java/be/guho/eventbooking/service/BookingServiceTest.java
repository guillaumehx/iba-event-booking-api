package be.guho.eventbooking.service;

import be.guho.eventbooking.api.exception.EventNotFoundException;
import be.guho.eventbooking.api.exception.NotEnoughSeatException;
import be.guho.eventbooking.dto.BookingStatus;
import be.guho.eventbooking.dto.CreateBookingRequest;
import be.guho.eventbooking.persistence.entity.BookingEntity;
import be.guho.eventbooking.persistence.entity.EventEntity;
import be.guho.eventbooking.persistence.repository.BookingRepository;
import be.guho.eventbooking.persistence.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final String USER_ID = "usr-01";
    private static final String EVENT_ID = "evt-01";

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EventEntity event;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldConfirmBookingAndUpdateBookedSeats() {
        when(eventRepository.findByIdLocked(EVENT_ID)).thenReturn(Optional.of(event));
        when(event.getId()).thenReturn(EVENT_ID);
        when(event.getCapacity()).thenReturn(10);
        when(event.getBookedSeats()).thenReturn(4);
        when(bookingRepository.save(any(BookingEntity.class))).then(returnsFirstArg());

        BookingEntity result = bookingService.bookSeats(USER_ID, new CreateBookingRequest(EVENT_ID, 3));

        verify(event).setBookedSeats(7);
        verify(eventRepository).save(event);

        ArgumentCaptor<BookingEntity> captor = ArgumentCaptor.forClass(BookingEntity.class);
        verify(bookingRepository).save(captor.capture());
        BookingEntity saved = captor.getValue();
        assertThat(saved.getEventId()).isEqualTo(EVENT_ID);
        assertThat(saved.getUserId()).isEqualTo(USER_ID);
        assertThat(saved.getSeats()).isEqualTo(3);
        assertThat(saved.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void shouldAcceptBookingThatExactlyFillsCapacity() {
        when(eventRepository.findByIdLocked(EVENT_ID)).thenReturn(Optional.of(event));
        when(event.getId()).thenReturn(EVENT_ID);
        when(event.getCapacity()).thenReturn(10);
        when(event.getBookedSeats()).thenReturn(7);
        when(bookingRepository.save(any(BookingEntity.class))).then(returnsFirstArg());

        bookingService.bookSeats(USER_ID, new CreateBookingRequest(EVENT_ID, 3));

        verify(event).setBookedSeats(10);
    }

    @Test
    void shouldThrowWhenCapacityOver() {
        when(eventRepository.findByIdLocked(EVENT_ID)).thenReturn(Optional.of(event));
        when(event.getCapacity()).thenReturn(10);
        when(event.getBookedSeats()).thenReturn(8);

        assertThatThrownBy(() -> bookingService.bookSeats(USER_ID, new CreateBookingRequest(EVENT_ID, 3)))
                .isInstanceOf(NotEnoughSeatException.class)
                .hasMessage("Not enough seats available for event with id 'evt-01' and capacity '10'");

        verify(event, never()).setBookedSeats(any(Integer.class));
        verify(eventRepository, never()).save(any());
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void shouldThrowWhenEventNotFound() {
        when(eventRepository.findByIdLocked(EVENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.bookSeats(USER_ID, new CreateBookingRequest(EVENT_ID, 3)))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Event not found with id 'evt-01'");

        verifyNoInteractions(bookingRepository);
    }
}
