package earlybird.earlybird.scheduler.notification.fcm.service.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeregisterFcmMessageAtSchedulerServiceRequest {
    private Long notificationId;
    private String deviceToken;
}
