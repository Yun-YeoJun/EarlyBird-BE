package earlybird.earlybird.scheduler.notification.fcm.controller.response;

import earlybird.earlybird.scheduler.notification.fcm.controller.request.RegisterNotificationByTokenRequest;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegisterNotificationByTokenResponse {
    private Long registeredNotificationId;

    @Builder
    private RegisterNotificationByTokenResponse(Long registeredNotificationId) {
        this.registeredNotificationId = registeredNotificationId;
    }

    public static RegisterNotificationByTokenResponse from(RegisterFcmMessageAtSchedulerServiceResponse serviceResponse) {
        return RegisterNotificationByTokenResponse.builder()
                .registeredNotificationId(serviceResponse.getId())
                .build();
    }
}
