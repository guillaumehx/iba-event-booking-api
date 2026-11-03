package be.guho.eventbooking.persistence.repository;

import be.guho.eventbooking.persistence.entity.EventEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends CrudRepository<EventEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> findByIdLocked(@Param("id") String id);
}
