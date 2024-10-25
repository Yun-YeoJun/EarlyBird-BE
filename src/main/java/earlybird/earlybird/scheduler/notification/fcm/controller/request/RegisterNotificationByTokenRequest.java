package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RegisterNotificationByTokenRequest {
    @NotBlank
    private String clientId;
    @NotBlank
    private String deviceToken;
    @NotBlank
    private String appointmentName;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime appointmentTime;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime preparationTime;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime movingTime;

    public RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest toRegisterFcmMessageForNewAppointmentAtSchedulerRequest() {
        return RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest.builder()
                .clientId(this.clientId)
                .deviceToken(this.deviceToken)
                .appointmentName(this.appointmentName)
                .appointmentTime(this.appointmentTime)
                .preparationTime(this.preparationTime)
                .movingTime(this.movingTime)
                .build();
    }
}
