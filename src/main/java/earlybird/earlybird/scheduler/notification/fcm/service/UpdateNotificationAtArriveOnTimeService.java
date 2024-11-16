package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.UpdateNotificationAtArriveOnTimeServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.CANCELLED_BY_ARRIVE_ON_TIME;

@RequiredArgsConstructor
@Service
public class UpdateNotificationAtArriveOnTimeService {

    private final AppointmentRepository appointmentRepository;
    private final DeregisterNotificationAtSchedulerService deregisterNotificationService;

    @Transactional
    public void update(UpdateNotificationAtArriveOnTimeServiceRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(AppointmentNotFoundException::new);

        if (!appointment.getClientId().equals(request.getClientId()))
            throw new AppointmentNotFoundException();

        DeregisterFcmMessageAtSchedulerServiceRequest deregisterRequest = DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .appointmentId(appointment.getId())
                .clientId(appointment.getClientId())
                .targetNotificationStatus(CANCELLED_BY_ARRIVE_ON_TIME)
                .build();

        deregisterNotificationService.deregister(deregisterRequest);
    }
}
