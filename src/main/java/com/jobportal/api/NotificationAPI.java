package com.jobportal.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.RespnseDTO;
import com.jobportal.entity.Notification;
import com.jobportal.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@Validated
@RequestMapping("/notification")
@CrossOrigin(origins = "*")
public class NotificationAPI {

        @Autowired
        private NotificationService notificationService;

        @GetMapping("/get/{userId}")
        public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
            return new ResponseEntity<>(notificationService.getUnreadNotifications(userId),HttpStatus.OK);
        }
        
        @PutMapping("/read/{id}")
        public ResponseEntity<RespnseDTO> readNotifications(@PathVariable Long id) throws Exception{
            notificationService.readNotifications(id);
            return new ResponseEntity<>(new RespnseDTO("Success"),HttpStatus.OK);
        }
}
