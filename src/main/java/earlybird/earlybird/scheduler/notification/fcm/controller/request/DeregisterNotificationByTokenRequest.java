package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeregisterNotificationByTokenRequest {

    @NotNull
    private Long notificationId;
    @NotBlank
    private String deviceToken;

    @Builder
    private DeregisterNotificationByTokenRequest(Long notificationId, String deviceToken) {
        this.notificationId = notificationId;
        this.deviceToken = deviceToken;
    }

    public DeregisterFcmMessageAtSchedulerServiceRequest toServiceRequest() {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .notificationId(notificationId)
                .deviceToken(deviceToken)
                .build();
    }
}
