package com.levi9.alertingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.levi9.alertingservice.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
