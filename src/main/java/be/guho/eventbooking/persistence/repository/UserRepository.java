package be.guho.eventbooking.persistence.repository;

import be.guho.eventbooking.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {}
