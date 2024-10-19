package earlybird.earlybird.scheduler.notification.fcm.service.response;

import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendMessageByTokenServiceResponse {
    private Long id;
    private NotificationStep notificationStep;
    private String deviceToken;
    private LocalDateTime targetTime;
    private NotificationStatus status;
    private String fcmMessageId;

    @Builder
    private SendMessageByTokenServiceResponse(
            Long id, NotificationStep notificationStep, String deviceToken, LocalDateTime targetTime,
            NotificationStatus status, String fcmMessageId
    ) {
        this.id = id;
        this.notificationStep = notificationStep;
        this.deviceToken = deviceToken;
        this.targetTime = targetTime;
        this.status = status;
        this.fcmMessageId = fcmMessageId;
    }

    public static SendMessageByTokenServiceResponse of(FcmNotification fcmNotification) {
        return SendMessageByTokenServiceResponse.builder()
                .id(fcmNotification.getId())
                .notificationStep(fcmNotification.getNotificationStep())
                .deviceToken(fcmNotification.getAppointment().getDeviceToken())
                .targetTime(fcmNotification.getTargetTime())
                .status(fcmNotification.getStatus())
                .build();
    }
}
