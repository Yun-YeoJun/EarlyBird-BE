package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;
import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.PENDING;

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
                    schedulingTaskListService.remove(notification.getUuid());
                    notification.updateToCancelled();
                });
    }

    private Appointment getAppointmentFrom(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Optional<Appointment> optionalAppointment = request.getOptionalAppointment();

        Appointment appointment = optionalAppointment.orElseGet(
                () -> appointmentRepository.findById(request.getAppointmentId())
                        .orElseThrow(AppointmentNotFoundException::new)
        );

        if (!appointment.getClientId().equals(request.getClientId())) {
            throw new AppointmentNotFoundException();
        }

        return appointment;
    }
}
