package earlybird.earlybird.scheduler.notification.fcm.service.response;

import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.PENDING;

@Getter
public class SendMessageByTokenServiceResponse {
    private Long id;
    private String uuid;
    private String title;
    private String body;
    private String deviceToken;
    private LocalDateTime targetTime;
    private FcmNotificationStatus status;
    private String fcmMessageId;

    @Builder
    private SendMessageByTokenServiceResponse(
            Long id, String uuid, String title, String body, String deviceToken, LocalDateTime targetTime,
            FcmNotificationStatus status, String fcmMessageId
    ) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
        this.targetTime = targetTime;
        this.status = status;
        this.fcmMessageId = fcmMessageId;
    }

    public static SendMessageByTokenServiceResponse of(FcmNotification fcmNotification) {
        return SendMessageByTokenServiceResponse.builder()
                .id(fcmNotification.getId())
                .uuid(fcmNotification.getUuid())
                .title(fcmNotification.getTitle())
                .body(fcmNotification.getBody())
                .deviceToken(fcmNotification.getAppointment().getDeviceToken())
                .targetTime(fcmNotification.getTargetTime())
                .status(fcmNotification.getStatus())
                .fcmMessageId(fcmNotification.getFcmMessageId())
                .build();
    }
}
