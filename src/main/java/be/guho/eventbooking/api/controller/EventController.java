package be.guho.eventbooking.api.controller;

import be.guho.eventbooking.api.ApiMapper;
import be.guho.eventbooking.api.EventsApi;
import be.guho.eventbooking.dto.CreateEventRequest;
import be.guho.eventbooking.dto.EventResponse;
import be.guho.eventbooking.persistence.entity.EventEntity;
import be.guho.eventbooking.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController implements EventsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final ApiMapper mapper;

    public EventController(EventService eventService, ApiMapper mapper) {
        this.eventService = eventService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<EventResponse> createEvent(String userId, CreateEventRequest createEventRequest) {
        LOGGER.debug("Executing createEvent with userId {} and createEventRequest {}", userId, createEventRequest);
        EventEntity entity = eventService.createEvent(createEventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toEventResponse(entity));
    }

    @Override
    public ResponseEntity<List<EventResponse>> getEvents() {
        LOGGER.debug("Executing getEvents");
        List<EventEntity> entities = eventService.getAllEvents();
        return ResponseEntity.ok(mapper.toEventResponseList(entities));
    }

}
