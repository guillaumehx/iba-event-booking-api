package be.guho.eventbooking.persistence.entity;

import be.guho.eventbooking.dto.BookingStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private int seats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected BookingEntity() {}

    public BookingEntity(String eventId, String userId, int seats, BookingStatus status, Instant createdAt) {
        this.eventId = eventId;
        this.userId = userId;
        this.seats = seats;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
