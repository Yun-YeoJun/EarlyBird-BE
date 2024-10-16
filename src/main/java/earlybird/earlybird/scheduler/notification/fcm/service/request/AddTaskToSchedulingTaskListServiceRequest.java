package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;

@Getter
@Builder
public class AddTaskToSchedulingTaskListServiceRequest {
    private String uuid;
    private Instant targetTime;
    private String title;
    private String body;
    private String deviceToken;
    private String clientId;
    private Appointment appointment;

    public static AddTaskToSchedulingTaskListServiceRequest of(FcmNotification notification) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .uuid(notification.getUuid())
                .appointment(notification.getAppointment())
                .deviceToken(notification.getAppointment().getDeviceToken())
                .title(notification.getTitle())
                .body(notification.getBody())
                .clientId(notification.getAppointment().getClientId())
                .targetTime(notification.getTargetTime().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .build();
    }
}
