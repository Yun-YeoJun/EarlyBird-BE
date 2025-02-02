package earlybird.earlybird.scheduler.notification.service.register.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterNotificationServiceResponse {

  private final Appointment appointment;
  private final List<FcmNotification> notifications;

  @Builder
  private RegisterNotificationServiceResponse(
      Appointment appointment, List<FcmNotification> notifications) {
    this.appointment = appointment;
    this.notifications = notifications;
  }

  public static RegisterNotificationServiceResponse of(Appointment appointment) {
    return RegisterNotificationServiceResponse.builder()
        .appointment(appointment)
        .notifications(appointment.getFcmNotifications())
        .build();
  }
}
