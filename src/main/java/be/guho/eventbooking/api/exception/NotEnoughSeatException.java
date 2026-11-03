package be.guho.eventbooking.api.exception;

public class NotEnoughSeatException extends RuntimeException {

    public NotEnoughSeatException(String eventId, int capacity) {
        super("Not enough seats available for event with id '" + eventId + "' and capacity '" + capacity + "'");
    }
}
