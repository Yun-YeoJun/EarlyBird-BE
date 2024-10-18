package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeregisterFcmMessageAtSchedulerServiceRequest {
    private String clientId;
    private Long appointmentId;
    private NotificationStatus targetNotificationStatus;
}
