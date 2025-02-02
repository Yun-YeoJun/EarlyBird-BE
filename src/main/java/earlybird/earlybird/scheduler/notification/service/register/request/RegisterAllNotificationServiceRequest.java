package earlybird.earlybird.scheduler.notification.service.register.request;

import earlybird.earlybird.appointment.domain.Appointment;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class RegisterAllNotificationServiceRequest {
    private Instant preparationTimeInstant;
    private Instant movingTimeInstant;
    private Instant appointmentTimeInstant;
    private Appointment appointment;
}
