package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.appointment.domain.AppointmentUpdateType;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationUpdateType;
import lombok.Builder;
import lombok.Getter;

import static earlybird.earlybird.appointment.domain.AppointmentUpdateType.*;

@Builder
@Getter
public class DeregisterFcmMessageAtSchedulerServiceRequest {
    private String clientId;
    private Long appointmentId;
    private NotificationStatus targetNotificationStatus;

    public static DeregisterFcmMessageAtSchedulerServiceRequest from(UpdateAppointmentServiceRequest request) {
        AppointmentUpdateType updateType = request.getUpdateType();
        NotificationStatus targetStatus = switch (updateType) {
            case POSTPONE -> NotificationStatus.POSTPONE;
            case MODIFY -> NotificationStatus.MODIFIED;
        };

        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .appointmentId(request.getAppointmentId())
                .clientId(request.getClientId())
                .targetNotificationStatus(targetStatus)
                .build();

    }
}
