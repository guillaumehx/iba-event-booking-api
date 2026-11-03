package be.guho.eventbooking.api;

import be.guho.eventbooking.dto.BookingResponse;
import be.guho.eventbooking.dto.EventResponse;

import be.guho.eventbooking.persistence.entity.BookingEntity;
import be.guho.eventbooking.persistence.entity.EventEntity;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    EventResponse toEventResponse(EventEntity entity);

    List<EventResponse> toEventResponseList(List<EventEntity> entities);

    BookingResponse toBookingResponse(BookingEntity entity);

    List<BookingResponse> toBookingResponseList(List<BookingEntity> entities);

    default OffsetDateTime map(Instant value) {
        if (value == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(value, ZoneOffset.UTC);
    }
}
