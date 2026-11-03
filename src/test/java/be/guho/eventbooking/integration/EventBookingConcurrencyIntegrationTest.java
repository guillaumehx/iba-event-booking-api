package be.guho.eventbooking.integration;

import be.guho.eventbooking.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EventBookingConcurrencyIntegrationTest extends IntegrationTest {

    private static final String USER = "usr-tst-2";

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void simultaneousBookingsShouldNotExceedEventCapacity() throws Exception {
        EventEntity event = eventRepository.save(new EventEntity("Concurrent booking test", Instant.now().plusSeconds(3600), 1));
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<Integer> firstBooking = executor.submit(() -> makeConcurrentBooking(event.getId(), USER, ready, start));
            Future<Integer> secondBooking = executor.submit(() -> makeConcurrentBooking(event.getId(), USER, ready, start));

            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertThat(List.of(firstBooking.get(10, TimeUnit.SECONDS), secondBooking.get(10, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(201, 422);
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
        }

        assertThat(bookedSeats(event.getId())).isEqualTo(event.getCapacity());
        assertThat(bookingRepository.findByUserId(USER).stream()
                .filter(booking -> booking.getEventId().equals(event.getId()))
                .count())
                .isEqualTo(1);
    }
}
