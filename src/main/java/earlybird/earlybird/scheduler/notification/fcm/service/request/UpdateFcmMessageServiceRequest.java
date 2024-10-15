package earlybird.earlybird.scheduler.notification.fcm.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder
@Getter
public class UpdateFcmMessageServiceRequest {
    private Long notificationId;
    private String deviceToken;
    private LocalDateTime targetTime;

    public Instant getTargetTimeInstant() {
        return targetTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }
}
