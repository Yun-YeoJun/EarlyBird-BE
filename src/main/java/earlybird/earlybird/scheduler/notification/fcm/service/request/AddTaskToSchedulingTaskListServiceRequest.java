package earlybird.earlybird.scheduler.notification.fcm.service.request;

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

    public static AddTaskToSchedulingTaskListServiceRequest of(FcmNotification notification) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .uuid(notification.getUuid())
                .deviceToken(notification.getDeviceToken())
                .title(notification.getTitle())
                .body(notification.getBody())
                .targetTime(notification.getTargetTime().atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .build();
    }
}
