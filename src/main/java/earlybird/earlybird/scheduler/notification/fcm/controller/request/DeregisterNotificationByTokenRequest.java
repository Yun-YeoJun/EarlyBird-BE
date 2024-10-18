package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.CANCELLED;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeregisterNotificationByTokenRequest {

    @NotBlank
    private String clientId;
    @NotNull
    private Long appointmentId;

    @Builder
    public DeregisterNotificationByTokenRequest(String clientId, Long appointmentId) {
        this.clientId = clientId;
        this.appointmentId = appointmentId;
    }

    public DeregisterFcmMessageAtSchedulerServiceRequest toServiceRequest() {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .appointmentId(appointmentId)
                .clientId(clientId)
                .targetNotificationStatus(CANCELLED)
                .build();
    }
}
