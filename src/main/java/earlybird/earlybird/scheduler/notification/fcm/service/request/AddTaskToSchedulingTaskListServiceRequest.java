package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@Builder
public class AddTaskToSchedulingTaskListServiceRequest {
    private Instant targetTime;
    private NotificationStep notificationStep;
    private String deviceToken;
    private String clientId;
    private Appointment appointment;
    private Long notificationId;

    public static AddTaskToSchedulingTaskListServiceRequest of(FcmNotification notification) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .appointment(notification.getAppointment())
                .deviceToken(notification.getAppointment().getDeviceToken())
                .notificationStep(notification.getNotificationStep())
                .clientId(notification.getAppointment().getClientId())
                .targetTime(notification.getTargetTime().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .notificationId(notification.getId())
                .build();
    }
}
