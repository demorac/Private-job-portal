package com.jobportal.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobportal.dto.NotificationSystem;
import com.jobportal.entity.Notification;



public interface NotificationRepo extends MongoRepository<Notification,Long>{
    public List<Notification> findByUserIdAndStatus(Long userId, NotificationSystem status);

}
