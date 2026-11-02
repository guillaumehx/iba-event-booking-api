INSERT INTO users (id, name, email)
SELECT id, name, email
FROM CSVREAD('./data/users.csv', NULL, 'charset=UTF-8');

INSERT INTO events (id, name, starts_at, capacity)
SELECT id, name, CAST(starts_at AS TIMESTAMP WITH TIME ZONE), CAST(capacity AS INT)
FROM CSVREAD('./data/events.csv', NULL, 'charset=UTF-8');

INSERT INTO bookings (id, event_id, user_id, seats, status, created_at)
SELECT id, event_id, user_id, CAST(seats AS INT), status,
       CAST(created_at AS TIMESTAMP WITH TIME ZONE)
FROM CSVREAD('./data/bookings.csv', NULL, 'charset=UTF-8');