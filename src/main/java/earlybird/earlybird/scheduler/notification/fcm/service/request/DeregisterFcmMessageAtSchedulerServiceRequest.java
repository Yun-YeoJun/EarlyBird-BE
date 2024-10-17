package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.appointment.domain.Appointment;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class DeregisterFcmMessageAtSchedulerServiceRequest {
    private String clientId;
    private Long appointmentId;
    private Optional<Appointment> optionalAppointment;
}
