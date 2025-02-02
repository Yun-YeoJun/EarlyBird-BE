package earlybird.earlybird.scheduler.notification.service.register.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest {
    private String clientId;
    private String deviceToken;
    private String appointmentName;
    private LocalDateTime appointmentTime;
    private LocalDateTime preparationTime;
    private LocalDateTime movingTime;

    @Builder
    private RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest(
            String clientId,
            String deviceToken,
            String appointmentName,
            LocalDateTime appointmentTime,
            LocalDateTime preparationTime,
            LocalDateTime movingTime) {
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
}
