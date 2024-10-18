package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import earlybird.earlybird.error.exception.NotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMessageToFcmService {

    @Value("${fcm.service-account-file}")
    private String serviceAccountFilePath;

    @Value("${fcm.project-id}")
    private String projectId;

    private final FcmNotificationRepository fcmNotificationRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    @PostConstruct
    private void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    @Transactional
    @Async
    public CompletableFuture<SendMessageByTokenServiceResponse> sendMessageByToken(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException {
        Long notificationId = request.getNotificationId();
        FcmNotification fcmNotification = fcmNotificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        String messageId = null;
        try {
            messageId = firebaseMessagingService.send(request);
        } catch (Exception e) {
            log.error("FCM send Error", e);
            throw e;
        }

        fcmNotification.onSendToFcmSuccess(messageId);
        fcmNotificationRepository.save(fcmNotification);

        return CompletableFuture.completedFuture(SendMessageByTokenServiceResponse.of(fcmNotification));
    }


}
