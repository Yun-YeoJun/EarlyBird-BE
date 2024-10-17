package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;

public interface FirebaseMessagingService {
    String send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException;
}
