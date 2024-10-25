package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.*;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultFirebaseMessagingService implements FirebaseMessagingService {

    private final FcmNotificationRepository fcmNotificationRepository;

    public void send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException {
        FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .setToken(request.getDeviceToken())
                .build());
    }

    @Transactional
    public void recover(SendMessageByTokenServiceRequest request) {
        fcmNotificationRepository.findById(request.getNotificationId())
                .ifPresent(FcmNotification::onSendToFcmFailure);
    }
}
