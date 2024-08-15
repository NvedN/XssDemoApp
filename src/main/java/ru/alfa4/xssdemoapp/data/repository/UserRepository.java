package ru.alfa4.xssdemoapp.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alfa4.xssdemoapp.data.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
