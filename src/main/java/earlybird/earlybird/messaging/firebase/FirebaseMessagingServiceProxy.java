package earlybird.earlybird.messaging.firebase;

import earlybird.earlybird.messaging.MessagingService;
import earlybird.earlybird.messaging.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.NotificationStatus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class FirebaseMessagingServiceProxy implements MessagingService {

    private final FirebaseMessagingService target;
    private final FcmNotificationRepository notificationRepository;
    private final ThreadPoolTaskExecutor taskExecutor;

    public FirebaseMessagingServiceProxy(
            FirebaseMessagingService target,
            FcmNotificationRepository notificationRepository,
            @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.target = target;
        this.notificationRepository = notificationRepository;
        this.taskExecutor = taskExecutor;
    }

    @Transactional
    @Override
    public void send(SendMessageByTokenServiceRequest request) {
        Long notificationId = request.getNotificationId();
        notificationRepository
                .findByIdAndStatusForUpdate(notificationId, NotificationStatus.PENDING)
                .ifPresent(
                        notification -> {
                            taskExecutor.execute(() -> target.send(request));
                            notification.onSendToFcmSuccess();
                        });
    }

    @Transactional
    @Override
    public void recover(SendMessageByTokenServiceRequest request) {
        target.recover(request);
    }
}
