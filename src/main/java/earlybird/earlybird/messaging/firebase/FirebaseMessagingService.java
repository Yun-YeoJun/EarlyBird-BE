package earlybird.earlybird.messaging.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import earlybird.earlybird.messaging.MessagingService;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.messaging.request.SendMessageByTokenServiceRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class FirebaseMessagingService implements MessagingService {

    @Value("${fcm.service-account-file}")
    private String serviceAccountFilePath;

    @Value("${fcm.project-id}")
    private String projectId;

    private final FcmNotificationRepository fcmNotificationRepository;

    @PostConstruct
    private void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            ClassPathResource resource = new ClassPathResource(serviceAccountFilePath);
            log.info("Loading Firebase credentials from {}", resource.getPath());
            log.info("File exists: {}", resource.exists());
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    public void send(SendMessageByTokenServiceRequest request) {
        log.info("send notification: {} {}", request.getNotificationId(), request.getTitle());
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .setToken(request.getDeviceToken())
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("send success");
    }

    @Transactional
    public void recover(SendMessageByTokenServiceRequest request) {
        log.error("recover notification: {} {}", request.getNotificationId(), request.getTitle());
        fcmNotificationRepository.findById(request.getNotificationId())
                .ifPresent(FcmNotification::onSendToFcmFailure);
    }
}
