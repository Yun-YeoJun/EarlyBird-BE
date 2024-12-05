package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;

import java.time.Instant;
import java.util.Map;

public interface RegisterNotificationService {
    void register(Appointment appointment, Map<NotificationStep, Instant> notificationStepAndTargetTime);
}
