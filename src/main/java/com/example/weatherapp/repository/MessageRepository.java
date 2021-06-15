package com.example.weatherapp.repository;

import com.example.weatherapp.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    List<MessageEntity> findAllByMessage(String message);
}
