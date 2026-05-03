package com.student.management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.management.enitity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
}