package com.sherif.myapi.repository;

import com.sherif.myapi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // You can define custom queries here if needed
}
