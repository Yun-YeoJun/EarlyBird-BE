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
    private String clientId;
    private String deviceToken;
    private String appointmentName;
    private LocalDateTime appointmentTime;
    private LocalDateTime preparationTime;
    private LocalDateTime movingTime;

    @Builder
    private RegisterFcmMessageAtSchedulerServiceRequest(String clientId, String deviceToken, String appointmentName,
                            LocalDateTime appointmentTime, LocalDateTime preparationTime, LocalDateTime movingTime) {
        this.clientId = clientId;
        this.deviceToken = deviceToken;
        this.appointmentName = appointmentName;
        this.appointmentTime = appointmentTime;
        this.preparationTime = preparationTime;
        this.movingTime = movingTime;
    }

    public Instant getAppointmentTimeInstant() {
        return appointmentTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

    public Instant getPreparationTimeInstant() {
        return preparationTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

    public Instant getMovingTimeInstant() {
        return movingTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

//    public FcmNotification toFcmNotification() {
//        return FcmNotification.builder()
//                .uuid(uuid)
//                .title(title)
//                .body(body)
//                .deviceToken(deviceToken)
//                .targetTime(targetTime)
//                .build();
//    }
}
