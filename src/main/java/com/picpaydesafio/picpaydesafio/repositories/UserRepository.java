package com.picpaydesafio.picpaydesafio.repositories;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDocument(String document);
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);
}
