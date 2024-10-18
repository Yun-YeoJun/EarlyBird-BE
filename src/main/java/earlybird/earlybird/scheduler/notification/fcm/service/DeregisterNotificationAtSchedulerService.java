package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.MODIFIED;
import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.PENDING;

@RequiredArgsConstructor
@Service
public class DeregisterNotificationAtSchedulerService {

    private final SchedulingTaskListService schedulingTaskListService;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void deregister(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Appointment appointment = getAppointmentFrom(request);

        appointment.getFcmNotifications().stream()
                .filter(notification -> notification.getStatus() == PENDING)
                .forEach(notification -> {
                    schedulingTaskListService.remove(notification.getId());
                    notification.updateStatusTo(request.getTargetNotificationStatus());
                });
    }

    private Appointment getAppointmentFrom(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(AppointmentNotFoundException::new);

        if (!appointment.getClientId().equals(request.getClientId())) {
            throw new AppointmentNotFoundException();
        }

        return appointment;
    }
}
