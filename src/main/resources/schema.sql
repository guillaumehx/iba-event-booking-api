CREATE TABLE users (
                       id    VARCHAR(20)  PRIMARY KEY,
                       name  VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE events (
                        id        VARCHAR(20)  PRIMARY KEY,
                        name      VARCHAR(255) NOT NULL,
                        starts_at TIMESTAMP WITH TIME ZONE NOT NULL,
                        capacity  INT NOT NULL CHECK (capacity > 0)
);

CREATE TABLE bookings (
                          id         VARCHAR(20) PRIMARY KEY,
                          event_id   VARCHAR(20) NOT NULL REFERENCES events(id),
                          user_id    VARCHAR(20) NOT NULL REFERENCES users(id),
                          seats      INT NOT NULL CHECK (seats > 0),
                          status     VARCHAR(20) NOT NULL CHECK (status IN ('CONFIRMED', 'CANCELLED')),
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_bookings_user  ON bookings(user_id);
CREATE INDEX idx_bookings_event ON bookings(event_id, status);