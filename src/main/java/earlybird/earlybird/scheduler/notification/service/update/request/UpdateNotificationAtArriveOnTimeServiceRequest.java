package earlybird.earlybird.scheduler.notification.service.update.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateNotificationAtArriveOnTimeServiceRequest {

    private final Long appointmentId;
    private final String clientId;

    @Builder
    private UpdateNotificationAtArriveOnTimeServiceRequest(Long appointmentId, String clientId) {
        this.appointmentId = appointmentId;
        this.clientId = clientId;
    }
}
