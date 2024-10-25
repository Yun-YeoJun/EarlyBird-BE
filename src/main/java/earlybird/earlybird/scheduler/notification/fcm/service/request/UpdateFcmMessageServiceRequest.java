package earlybird.earlybird.scheduler.notification.fcm.service.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder
@Getter
public class UpdateFcmMessageServiceRequest {

    private Long appointmentId;
    private String appointmentName;
    private String clientId;
    private String deviceToken;
    private LocalDateTime preparationTime;
    private LocalDateTime movingTime;
    private LocalDateTime appointmentTime;

    public Instant getAppointmentTimeInstant() {
        return appointmentTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

    public Instant getPreparationTimeInstant() {
        return preparationTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }

    public Instant getMovingTimeInstant() {
        return movingTime.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    }
}
