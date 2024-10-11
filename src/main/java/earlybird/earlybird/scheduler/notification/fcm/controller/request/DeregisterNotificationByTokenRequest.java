package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeregisterNotificationByTokenRequest {

    @NotNull
    private Long notificationId;
    @NotBlank
    private String deviceToken;

    public DeregisterFcmMessageAtSchedulerServiceRequest toServiceRequest() {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .notificationId(notificationId)
                .deviceToken(deviceToken)
                .build();
    }
}
