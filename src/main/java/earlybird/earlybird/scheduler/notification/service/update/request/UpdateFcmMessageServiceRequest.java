package earlybird.earlybird.scheduler.notification.service.update.request;

import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateFcmMessageServiceRequest {

  private Long appointmentId;
  private String appointmentName;
  private String clientId;
  private String deviceToken;
  private LocalDateTime preparationTime;
  private LocalDateTime movingTime;
  private LocalDateTime appointmentTime;
  private NotificationUpdateType updateType;
}
