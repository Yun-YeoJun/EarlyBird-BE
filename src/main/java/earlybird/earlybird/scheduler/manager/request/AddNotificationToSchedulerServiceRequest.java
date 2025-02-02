package earlybird.earlybird.scheduler.manager.request;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@Builder
public class AddNotificationToSchedulerServiceRequest {
    private Instant targetTime;
    private NotificationStep notificationStep;
    private String deviceToken;
    private String clientId;
    private Appointment appointment;
    private Long notificationId;

    public static AddNotificationToSchedulerServiceRequest of(FcmNotification notification) {
        return AddNotificationToSchedulerServiceRequest.builder()
                .appointment(notification.getAppointment())
                .deviceToken(notification.getAppointment().getDeviceToken())
                .notificationStep(notification.getNotificationStep())
                .clientId(notification.getAppointment().getClientId())
                .targetTime(
                        notification.getTargetTime().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .notificationId(notification.getId())
                .build();
    }
}
