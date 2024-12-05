package earlybird.earlybird.scheduler.notification.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateFcmMessageServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateNotificationRequest {

    @NotNull
    private Long appointmentId;
    @NotBlank
    private String appointmentName;
    @NotBlank
    private String clientId;
    @NotBlank
    private String deviceToken;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime appointmentTime;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime preparationTime;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime movingTime;
    @NotNull
    private NotificationUpdateType updateType;

    public UpdateFcmMessageServiceRequest toServiceRequest() {
        return UpdateFcmMessageServiceRequest.builder()
                .appointmentId(appointmentId)
                .appointmentName(appointmentName)
                .clientId(clientId)
                .deviceToken(deviceToken)
                .preparationTime(preparationTime)
                .movingTime(movingTime)
                .appointmentTime(appointmentTime)
                .updateType(updateType)
                .build();
    }
}
