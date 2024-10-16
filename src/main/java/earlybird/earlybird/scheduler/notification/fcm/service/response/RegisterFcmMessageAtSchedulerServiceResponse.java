package earlybird.earlybird.scheduler.notification.fcm.service.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RegisterFcmMessageAtSchedulerServiceResponse {

    private final Appointment appointment;
    private final List<FcmNotification> notifications;

    @Builder
    private RegisterFcmMessageAtSchedulerServiceResponse(Appointment appointment, List<FcmNotification> notifications) {
        this.appointment = appointment;
        this.notifications = notifications;
    }

    public static RegisterFcmMessageAtSchedulerServiceResponse of(Appointment appointment) {
        return RegisterFcmMessageAtSchedulerServiceResponse.builder()
                .appointment(appointment)
                .notifications(appointment.getFcmNotifications())
                .build();
    }
}
