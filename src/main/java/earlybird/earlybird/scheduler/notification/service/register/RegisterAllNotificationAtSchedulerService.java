package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
public class RegisterAllNotificationAtSchedulerService implements RegisterNotificationService {

    private final RegisterOneNotificationAtSchedulerService registerOneNotificationService;

    public RegisterAllNotificationAtSchedulerService(
            FcmNotificationRepository fcmNotificationRepository,
            NotificationSchedulerManager notificationSchedulerManager) {
        this.registerOneNotificationService =
                new RegisterOneNotificationAtSchedulerService(
                        fcmNotificationRepository, notificationSchedulerManager);
    }

    @Override
    @Transactional
    public void register(
            Appointment appointment, Map<NotificationStep, Instant> notificationStepAndTargetTime) {
        for (NotificationStep notificationStep : notificationStepAndTargetTime.keySet()) {
            registerOneNotificationService.register(
                    appointment,
                    Map.of(notificationStep, notificationStepAndTargetTime.get(notificationStep)));
        }
    }
}
