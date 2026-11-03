package be.guho.eventbooking.api.exception;

public class EventDateNotInFutureException extends RuntimeException {

    public EventDateNotInFutureException() {
        super("Event date must be in the future");
    }
}
