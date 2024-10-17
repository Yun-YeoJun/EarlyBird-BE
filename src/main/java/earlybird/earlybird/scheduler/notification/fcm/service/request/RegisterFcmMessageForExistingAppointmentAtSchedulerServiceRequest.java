package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.appointment.domain.Appointment;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest {
    private String clientId;
    private String deviceToken;
    private LocalDateTime appointmentTime;
    private LocalDateTime preparationTime;
    private LocalDateTime movingTime;
    private Appointment appointment;

    @Builder
    private RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest(String clientId, String deviceToken,
                                                                              LocalDateTime appointmentTime, LocalDateTime preparationTime, LocalDateTime movingTime, Appointment appointment) {
        this.clientId = clientId;
        this.deviceToken = deviceToken;
        this.appointmentTime = appointmentTime;
        this.preparationTime = preparationTime;
        this.movingTime = movingTime;
        this.appointment = appointment;
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

    public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request, Appointment appointment) {
        return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .appointment(appointment)
                .preparationTime(request.getPreparationTime())
                .movingTime(request.getMovingTime())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }

    public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(UpdateFcmMessageServiceRequest request, Appointment appointment) {
        return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .appointment(appointment)
                .preparationTime(request.getPreparationTime())
                .movingTime(request.getMovingTime())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }
}
