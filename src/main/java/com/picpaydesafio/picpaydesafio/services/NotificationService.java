package com.picpaydesafio.picpaydesafio.services;

import com.picpaydesafio.picpaydesafio.domain.user.User;
import com.picpaydesafio.picpaydesafio.dtos.notification.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        String url = "https://util.devi.tools/api/v2/notify";
        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(url, notificationRequest, String.class);

        if(notificationResponse.getStatusCode() != HttpStatus.OK){
            System.out.println("Error while sending notification");
            throw new Exception("Notification service offline");
        }
    }
}
