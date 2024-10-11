package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
public class RegisterFcmMessageAtSchedulerServiceRequest {
    private String title;
    private String body;
    private String deviceToken;
    private LocalDateTime targetTime;
    private String uuid;

    @Builder
    private RegisterFcmMessageAtSchedulerServiceRequest(String title, String body, String deviceToken, LocalDateTime targetTime) {
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
        this.targetTime = targetTime;
        setUuid();
    }

    private void setUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Instant getTargetTimeInstant() {
        return targetTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

    public FcmNotification toFcmNotification() {
        return FcmNotification.builder()
                .uuid(uuid)
                .title(title)
                .body(body)
                .deviceToken(deviceToken)
                .targetTime(targetTime)
                .build();
    }
}
