package be.guho.eventbooking.service;

import be.guho.eventbooking.api.exception.EventNotFoundException;
import be.guho.eventbooking.api.exception.NotEnoughSeatException;
import be.guho.eventbooking.dto.BookingStatus;
import be.guho.eventbooking.dto.CreateBookingRequest;
import be.guho.eventbooking.persistence.entity.BookingEntity;
import be.guho.eventbooking.persistence.entity.EventEntity;
import be.guho.eventbooking.persistence.repository.BookingRepository;
import be.guho.eventbooking.persistence.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public BookingService(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingEntity bookSeats(String userId, CreateBookingRequest request) {
        LOGGER.debug("Processing to book {} seats for event {} by user {}", request.getSeats(), request.getEventId(), userId);
        EventEntity event = eventRepository
                .findByIdLocked(request.getEventId())
                .orElseThrow(() -> {
                    LOGGER.warn("Booking failed because event not found with Id {}", request.getEventId());
                    return new EventNotFoundException(request.getEventId());
                });

        int currentBooked = event.getBookedSeats();
        int requestedSeats = request.getSeats();
        int available = event.getCapacity() - currentBooked;

        if (requestedSeats > available) {
            LOGGER.warn("Booking rejected for event {} with {} requested seats. Only {} available (capacity: {})", request.getEventId(), requestedSeats, available, event.getCapacity());
            throw new NotEnoughSeatException(request.getEventId(), event.getCapacity());
        }

        event.setBookedSeats(currentBooked + requestedSeats);
        eventRepository.save(event);

        BookingEntity booking = new BookingEntity(event.getId(), userId, requestedSeats, BookingStatus.CONFIRMED, Instant.now());

        return bookingRepository.save(booking);
    }

    public List<BookingEntity> getUserBookings(String userId) {
        List<BookingEntity> bookingEntities= bookingRepository.findByUserId(userId);
        LOGGER.debug("Retrieved {} bookings for user {}", bookingEntities.size(), userId);
        return bookingEntities;
    }
}