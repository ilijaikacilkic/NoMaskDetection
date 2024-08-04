package com.levi9.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.levi9.authservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);
}
