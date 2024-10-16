package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import earlybird.earlybird.scheduler.notification.fcm.service.request.UpdateFcmMessageServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class UpdateNotificationRequest {

    @NotNull
    private Long notificationId;
    @NotBlank
    private String deviceToken;
    @NotBlank
    private String targetTime;

    public LocalDateTime getTargetTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.targetTime, formatter);
    }

    public UpdateFcmMessageServiceRequest toServiceRequest() {
        return UpdateFcmMessageServiceRequest.builder()
                .notificationId(this.notificationId)
                .deviceToken(this.deviceToken)
                .targetTime(this.getTargetTime())
                .build();
    }
}
