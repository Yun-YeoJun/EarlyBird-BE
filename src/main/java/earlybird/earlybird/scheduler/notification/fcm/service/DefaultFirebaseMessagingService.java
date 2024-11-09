package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.*;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultFirebaseMessagingService implements FirebaseMessagingService {

    private final FcmNotificationRepository fcmNotificationRepository;

    public void send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException {
        log.info("send notification: {} {}", request.getNotificationId(), request.getTitle());
        FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .setToken(request.getDeviceToken())
                .build());
        log.info("send success");
    }

    @Transactional
    public void recover(SendMessageByTokenServiceRequest request) {
        log.error("recover notification: {} {}", request.getNotificationId(), request.getTitle());
        fcmNotificationRepository.findById(request.getNotificationId())
                .ifPresent(FcmNotification::onSendToFcmFailure);
    }
}
