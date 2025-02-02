package earlybird.earlybird.scheduler.notification.service.register.request;

import earlybird.earlybird.appointment.domain.Appointment;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterAllNotificationServiceRequest {
  private Instant preparationTimeInstant;
  private Instant movingTimeInstant;
  private Instant appointmentTimeInstant;
  private Appointment appointment;
}
