package earlybird.earlybird.scheduler.notification.service.update.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.NotificationStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateFcmMessageServiceResponse {

  private final Appointment appointment;
  private final List<FcmNotification> notifications;

  public static UpdateFcmMessageServiceResponse of(Appointment appointment) {
    return UpdateFcmMessageServiceResponse.builder()
        .appointment(appointment)
        .notifications(
            appointment.getFcmNotifications().stream()
                .filter(notification -> notification.getStatus().equals(NotificationStatus.PENDING))
                .toList())
        .build();
  }
}
