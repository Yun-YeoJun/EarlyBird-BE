package earlybird.earlybird.messaging.request;

import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;
import lombok.Builder;
import lombok.Getter;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStep.ONE_HOUR_BEFORE_PREPARATION_TIME;

@Getter
public class SendMessageByTokenServiceRequest {
    private String title;
    private String body;
    private String deviceToken;
    private Long notificationId;

    @Builder
    private SendMessageByTokenServiceRequest(String title, String body, String deviceToken, Long notificationId) {
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
        this.notificationId = notificationId;
    }

    public static SendMessageByTokenServiceRequest from(AddNotificationToSchedulerServiceRequest request) {

        NotificationStep notificationStep = request.getNotificationStep();
        String appointmentName = request.getAppointment().getAppointmentName();
        String title = (notificationStep.equals(ONE_HOUR_BEFORE_PREPARATION_TIME))
                ? appointmentName + notificationStep.getTitle() : notificationStep.getTitle();

        return SendMessageByTokenServiceRequest.builder()
                .title(title)
                .body(notificationStep.getBody())
                .deviceToken(request.getDeviceToken())
                .notificationId(request.getNotificationId())
                .build();
    }
}
