package earlybird.earlybird.scheduler.notification.service.update.request;

import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
