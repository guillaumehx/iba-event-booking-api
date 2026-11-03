package be.guho.eventbooking.service;

import be.guho.eventbooking.dto.CreateEventRequest;
import be.guho.eventbooking.persistence.entity.EventEntity;
import be.guho.eventbooking.persistence.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventEntity createEvent(CreateEventRequest request) {
        EventEntity savedEvent = eventRepository.save(new EventEntity(request.getName(), request.getDate().toInstant(), request.getCapacity()));
        LOGGER.debug("Event created with Id {} ({})", savedEvent.getId(), savedEvent.getName());
        return savedEvent;
    }

    public List<EventEntity> getAllEvents() {
        List<EventEntity> events = (List<EventEntity>) eventRepository.findAll();
        LOGGER.debug("Retrieved {} events", events.size());
        return events;
    }
}
