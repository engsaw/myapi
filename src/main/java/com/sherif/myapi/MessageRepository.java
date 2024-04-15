package com.sherif.myapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // You can define custom queries here if needed
}
