# Event Booking API

Hi, and thanks for your interest in joining us!

Before the interview, we'd like you to build a small HTTP API.  
During the interview, we'll go through your code together and use it as the basis for our discussion.

## The problem

We sell seats for events (concerts, talks, workshops). Build an HTTP API that lets:

1. **Create an event** with a name, a date and a **capacity** (number of seats).
2. **Book seats** for an event as a user. A booking must never exceed the event's capacity.
3. **List a user's bookings.**

How you identify the user is up to you (a header is fine; no need for real authentication).

## Sample data

`data/` contains optional starting data: `users.csv`, `events.csv` and `bookings.csv`. Use it
however you like (seed your storage, fixtures for tests...) or ignore it.

- Dates are ISO 8601, in UTC.
- A booking's `status` is `CONFIRMED` or `CANCELLED`. Cancelled bookings don't hold seats.

## Rules

- **Any language, any framework, any storage**.
- **AI tools are allowed.** Use them as you would at work.
- **Extras are welcome.** Anything you think a production-ready service would need is fair game.
- Send us a link to a git repository or a zip file before the interview.
