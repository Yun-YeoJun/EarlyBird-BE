package earlybird.earlybird.scheduler.notification.service.update;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.CANCELLED_BY_ARRIVE_ON_TIME;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterNotificationServiceRequestFactory;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateNotificationAtArriveOnTimeServiceRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateNotificationAtArriveOnTimeService {

    private final AppointmentRepository appointmentRepository;
    private final DeregisterNotificationService deregisterNotificationService;

    @Transactional
    public void update(UpdateNotificationAtArriveOnTimeServiceRequest request) {
        Appointment appointment =
                appointmentRepository
                        .findById(request.getAppointmentId())
                        .orElseThrow(AppointmentNotFoundException::new);

        if (!isValidClientId(appointment, request.getClientId()))
            throw new AppointmentNotFoundException();

        DeregisterFcmMessageAtSchedulerServiceRequest deregisterRequest =
                DeregisterNotificationServiceRequestFactory.create(
                        appointment.getId(),
                        appointment.getClientId(),
                        CANCELLED_BY_ARRIVE_ON_TIME);

        deregisterNotificationService.deregister(deregisterRequest);
    }

    private boolean isValidClientId(Appointment appointment, String requestedClientId) {
        return appointment.getClientId().equals(requestedClientId);
    }
}
