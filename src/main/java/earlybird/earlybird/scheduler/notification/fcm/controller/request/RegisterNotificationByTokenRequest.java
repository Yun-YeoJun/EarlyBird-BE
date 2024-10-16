package earlybird.earlybird.scheduler.notification.fcm.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    /**
     * format: "yyyy-MM-dd HH:mm:ss"
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime appointmentTime;
    /**
     * format: "yyyy-MM-dd HH:mm:ss"
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime preparationTime;
    /**
     * format: "yyyy-MM-dd HH:mm:ss"
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime movingTime;


//    public LocalDateTime getAppointmentTime() {
//         final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        return LocalDateTime.parse(this.appointmentTime, formatter);
//    }
//
//    public LocalDateTime getPreparationTime() {
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        return LocalDateTime.parse(this.preparationTime, formatter);
//    }
//
//    public LocalDateTime getMovingTime() {
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        return LocalDateTime.parse(this.movingTime, formatter);
//    }



    public RegisterFcmMessageAtSchedulerServiceRequest toRegisterFcmMessageAtSchedulerRequest() {
        return RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .clientId(this.clientId)
                .deviceToken(this.deviceToken)
                .appointmentName(this.appointmentName)
                .appointmentTime(this.appointmentTime)
                .preparationTime(this.preparationTime)
                .movingTime(this.movingTime)
                .build();
    }
}
