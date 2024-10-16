package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.*;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import org.springframework.stereotype.Service;

import static com.google.firebase.messaging.AndroidConfig.Priority.HIGH;

@Service
public class DefaultFirebaseMessagingService implements FirebaseMessagingService {
    public String send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .setToken(request.getDeviceToken())
                .build());
    }
}
