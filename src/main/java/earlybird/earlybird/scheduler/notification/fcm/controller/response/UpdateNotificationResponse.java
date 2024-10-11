package earlybird.earlybird.scheduler.notification.fcm.controller.response;

import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateNotificationResponse {
    private Long notificationId;
    private LocalDateTime updatedTargetTime;

    @Builder
    private UpdateNotificationResponse(Long notificationId, LocalDateTime updatedTargetTime) {
        this.notificationId = notificationId;
        this.updatedTargetTime = updatedTargetTime;
    }

    public static UpdateNotificationResponse from(UpdateFcmMessageServiceResponse serviceResponse) {
        return UpdateNotificationResponse.builder()
                .notificationId(serviceResponse.getNotificationId())
                .updatedTargetTime(serviceResponse.getUpdatedTargetTime())
                .build();
    }
}
