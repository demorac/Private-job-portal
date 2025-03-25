package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.dto.NotificationDTO;
import com.jobportal.dto.NotificationSystem;
import com.jobportal.entity.Notification;
import com.jobportal.repository.NotificationRepo;
import com.jobportal.utility.Utilities;

@Service(value = "notificationService")
public class NotificationServiceImpl implements NotificationService{

    private final Utilities utilities;

    public NotificationServiceImpl(Utilities utilities) {
        this.utilities = utilities;
    }

    @Autowired 
    private NotificationRepo notificationRepo;

    @Override
public void sendNotification(NotificationDTO notificationDTO) throws Exception {
    notificationDTO.setId(utilities.getNextSequence("notification"));
    notificationDTO.setStatus(NotificationSystem.UNREAD);
    notificationDTO.setTimeStamps(LocalDateTime.now());
    notificationRepo.save(notificationDTO.toEntity());
}


    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
      return notificationRepo.findByUserIdAndStatus(userId, NotificationSystem.UNREAD);
    }


    @Override
    public void readNotifications(Long id) throws Exception{
      Notification noti=notificationRepo.findById(id).orElseThrow(()->new Exception("No Notification Found"));
      noti.setStatus(NotificationSystem.READ);
      notificationRepo.save(noti);
    }

}
