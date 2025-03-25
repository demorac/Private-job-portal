package com.jobportal.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jobportal.dto.NotificationDTO;
import com.jobportal.dto.NotificationSystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification")
public class Notification {
    private Long id;
    private Long userId;
    private String message;
    private String action;
    private String route;
    private LocalDateTime timeStamps;
    private NotificationSystem status;

    public NotificationDTO toDTO(){
        return new NotificationDTO(this.id,this.userId,this.message,this.action,this.route,this.timeStamps,this.status);
    }


}
