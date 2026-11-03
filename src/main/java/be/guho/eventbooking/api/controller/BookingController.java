package be.guho.eventbooking.api.controller;

import be.guho.eventbooking.api.ApiMapper;
import be.guho.eventbooking.api.BookingsApi;
import be.guho.eventbooking.dto.BookingResponse;
import be.guho.eventbooking.dto.CreateBookingRequest;
import be.guho.eventbooking.persistence.entity.BookingEntity;
import be.guho.eventbooking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController implements BookingsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final ApiMapper mapper;

    public BookingController(BookingService bookingService, ApiMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<BookingResponse> bookSeats(String userId, CreateBookingRequest createBookingRequest) {
        LOGGER.debug("Executing bookSeats with userId {} and createBookingRequest {}", userId, createBookingRequest);
        BookingEntity entity = bookingService.bookSeats(userId, createBookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toBookingResponse(entity));
    }

    @Override
    public ResponseEntity<List<BookingResponse>> getUserBookings(String userId) {
        LOGGER.debug("Executing getUserBookings with userId {}", userId);
        List<BookingEntity> entities = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(mapper.toBookingResponseList(entities));
    }
}
