package earlybird.earlybird.scheduler.notification.fcm.service.response;

import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateFcmMessageServiceResponse {
    private Long notificationId;
    private LocalDateTime updatedTargetTime;

    public static UpdateFcmMessageServiceResponse of(FcmNotification notification) {
        return UpdateFcmMessageServiceResponse.builder()
                .notificationId(notification.getId())
                .updatedTargetTime(notification.getTargetTime())
                .build();
    }
}
