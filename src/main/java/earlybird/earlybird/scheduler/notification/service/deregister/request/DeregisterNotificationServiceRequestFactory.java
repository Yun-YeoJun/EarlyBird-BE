package earlybird.earlybird.scheduler.notification.service.deregister.request;

import earlybird.earlybird.appointment.domain.AppointmentUpdateType;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.scheduler.notification.domain.NotificationStatus;
import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import org.springframework.stereotype.Service;

import static earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType.MODIFY;
import static earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType.POSTPONE;

public class DeregisterNotificationServiceRequestFactory {
    public static DeregisterFcmMessageAtSchedulerServiceRequest create(
            Long appointmentId, String clientId, NotificationStatus notificationStatus
    ) {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .appointmentId(appointmentId)
                .clientId(clientId)
                .targetNotificationStatus(notificationStatus)
                .build();
    }

    public static DeregisterFcmMessageAtSchedulerServiceRequest create(
            Long appointmentId, String clientId, NotificationUpdateType updateType
    ) {
        NotificationStatus targetStatus;
        if (updateType.equals(POSTPONE))
            targetStatus = NotificationStatus.POSTPONE;
        else if (updateType.equals(MODIFY))
            targetStatus = NotificationStatus.MODIFIED;
        else
            throw new IllegalArgumentException("Invalid update type: " + updateType);

        return create(appointmentId, clientId, targetStatus);
    }

    public static DeregisterFcmMessageAtSchedulerServiceRequest create(UpdateAppointmentServiceRequest request) {
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
