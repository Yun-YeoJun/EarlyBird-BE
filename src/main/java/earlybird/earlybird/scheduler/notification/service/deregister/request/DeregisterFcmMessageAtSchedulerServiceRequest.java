package earlybird.earlybird.scheduler.notification.service.deregister.request;

import earlybird.earlybird.scheduler.notification.domain.NotificationStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeregisterFcmMessageAtSchedulerServiceRequest {
    private String clientId;
    private Long appointmentId;
    private NotificationStatus targetNotificationStatus;
}
