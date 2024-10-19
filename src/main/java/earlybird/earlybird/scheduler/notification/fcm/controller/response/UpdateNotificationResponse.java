package earlybird.earlybird.scheduler.notification.fcm.controller.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateNotificationResponse {

    private final Appointment appointment;
    private final List<FcmNotification> notifications;

    @Builder
    public UpdateNotificationResponse(Appointment appointment, List<FcmNotification> notifications) {
        this.appointment = appointment;
        this.notifications = notifications;
    }

    public static UpdateNotificationResponse from(UpdateFcmMessageServiceResponse serviceResponse) {
        return UpdateNotificationResponse.builder()
                .appointment(serviceResponse.getAppointment())
                .notifications(serviceResponse.getNotifications())
                .build();
    }
}
