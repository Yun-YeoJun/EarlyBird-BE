package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import earlybird.earlybird.error.exception.NotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SendMessageToFcmService {

    @Value("${fcm.service-account-file}")
    private String serviceAccountFilePath;

    @Value("${fcm.project-id}")
    private String projectId;

    private final FcmNotificationRepository fcmNotificationRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final ThreadPoolTaskExecutor taskExecutor;

    public SendMessageToFcmService(
            FcmNotificationRepository fcmNotificationRepository,
            FirebaseMessagingService firebaseMessagingService,
            @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.fcmNotificationRepository = fcmNotificationRepository;
        this.firebaseMessagingService = firebaseMessagingService;
        this.taskExecutor = taskExecutor;
    }

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
    public void sendMessageByToken(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException {

        Long notificationId = request.getNotificationId();

        fcmNotificationRepository.findByIdAndStatusForUpdate(notificationId, NotificationStatus.PENDING)
                .ifPresent(notification -> {
                    taskExecutor.execute(() -> {
                        try {
                            firebaseMessagingService.send(request);
                        } catch (Exception e) {
                            log.error("FCM send Error", e);
                        }
                    });
                    notification.onSendToFcmSuccess();
                });
    }


}
