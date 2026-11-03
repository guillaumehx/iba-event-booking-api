package be.guho.eventbooking.persistence.repository;

import be.guho.eventbooking.persistence.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<BookingEntity, String> {
    List<BookingEntity> findByUserId(String userId);
}
