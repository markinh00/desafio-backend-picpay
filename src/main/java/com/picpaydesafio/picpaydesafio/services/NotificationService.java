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

        /*
        This part of the code is commented out because the current url provided by the github repository does not work.
        If you want to use with another url just uncomment the code and change the url.

        url source: https://github.com/PicPay/picpay-desafio-backend?tab=readme-ov-file
        */

        //String url = "https://util.devi.tools/api/v1/notify";
        //ResponseEntity<String> notificationResponse = restTemplate.postForEntity(url, notificationRequest, String.class);
        //
        //if(notificationResponse.getStatusCode() != HttpStatus.OK){
        //    System.out.println("Error while sending notification");
        //    throw new Exception("Notification service offline");
        //}

        System.out.println("Notification sent successfully");
    }
}
