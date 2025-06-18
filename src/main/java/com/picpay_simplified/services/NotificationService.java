package com.picpay_simplified.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpay_simplified.domain.user.User;
import com.picpay_simplified.dtos.NotificationDTO;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception{
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> response = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

        if(response.getStatusCode() != HttpStatus.OK){
            System.out.println("Erro ao enviar a notificação.");
            throw new Exception("Serviço de notificação fora do ar.");
        }
    }
}
