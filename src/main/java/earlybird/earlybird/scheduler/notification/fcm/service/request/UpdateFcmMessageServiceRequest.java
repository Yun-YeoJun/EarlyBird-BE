package earlybird.earlybird.scheduler.notification.fcm.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateFcmMessageServiceRequest {
    private Long notificationId;
    private String deviceToken;
    private LocalDateTime targetTime;
}
