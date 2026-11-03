package be.guho.eventbooking.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "starts_at", nullable = false)
    private Instant date;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "booked_seats", nullable = false)
    private int bookedSeats = 0;

    protected EventEntity() {}

    public EventEntity(String name, Instant date, int capacity) {
        this.name = name;
        this.date = date;
        this.capacity = capacity;
        this.bookedSeats = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}