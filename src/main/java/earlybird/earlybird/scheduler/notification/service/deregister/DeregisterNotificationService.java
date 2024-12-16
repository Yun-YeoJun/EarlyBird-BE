package earlybird.earlybird.scheduler.notification.service.deregister;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.service.FindAppointmentService;
import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.CANCELLED;
import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.PENDING;

@RequiredArgsConstructor
@Service
public class DeregisterNotificationService {

    private final NotificationSchedulerManager notificationSchedulerManager;
    private final FindAppointmentService findAppointmentService;

    @Transactional
    public void deregister(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Appointment appointment = findAppointmentService.findBy(request);

        appointment.getFcmNotifications().stream()
                .filter(notification -> notification.getStatus() == PENDING)
                .forEach(notification -> {
                    notificationSchedulerManager.remove(notification.getId());
                    notification.updateStatusTo(request.getTargetNotificationStatus());
                });

        if (request.getTargetNotificationStatus().equals(CANCELLED)) {
            appointment.setRepeatingDaysEmpty();
            appointment.setDeleted();
        }
    }
}
