package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RegisterNotificationByTokenRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    @NotBlank
    private String deviceToken;
    /**
     * format: "yyyy-MM-dd HH:mm:ss"
     */
    @NotBlank
    private String targetTime;

    public LocalDateTime getTargetTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.targetTime, formatter);
    }

    public RegisterFcmMessageAtSchedulerServiceRequest toRegisterFcmMessageAtSchedulerRequest() {
        return RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .title(this.title)
                .body(this.body)
                .deviceToken(deviceToken)
                .targetTime(getTargetTime())
                .build();
    }
}
