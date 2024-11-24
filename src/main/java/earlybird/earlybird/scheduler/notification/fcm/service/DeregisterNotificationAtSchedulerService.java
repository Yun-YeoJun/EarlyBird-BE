package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.appointment.service.FindAppointmentService;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.CANCELLED;
import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.PENDING;

@RequiredArgsConstructor
@Service
public class DeregisterNotificationAtSchedulerService {

    private final SchedulingTaskListService schedulingTaskListService;
    private final FindAppointmentService findAppointmentService;

    @Transactional
    public void deregister(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Appointment appointment = findAppointmentService.findBy(request);

        appointment.getFcmNotifications().stream()
                .filter(notification -> notification.getStatus() == PENDING)
                .forEach(notification -> {
                    schedulingTaskListService.remove(notification.getId());
                    notification.updateStatusTo(request.getTargetNotificationStatus());
                });

        if (request.getTargetNotificationStatus().equals(CANCELLED)) {
            appointment.setDeleted();
        }
    }
}
