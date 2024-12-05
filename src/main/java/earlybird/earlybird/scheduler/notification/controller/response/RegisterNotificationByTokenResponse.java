package earlybird.earlybird.scheduler.notification.controller.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.service.register.response.RegisterNotificationServiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RegisterNotificationByTokenResponse {
    private final Long appointmentId;
    private final String appointmentName;
    private final String clientId;
    private final String deviceToken;
    private final List<FcmNotification> notifications;

    @Builder
    private RegisterNotificationByTokenResponse(Long appointmentId, String appointmentName, String clientId, String deviceToken, List<FcmNotification> notifications) {
        this.appointmentId = appointmentId;
        this.appointmentName = appointmentName;
        this.clientId = clientId;
        this.deviceToken = deviceToken;
        this.notifications = notifications;
    }

    public static RegisterNotificationByTokenResponse from(RegisterNotificationServiceResponse serviceResponse) {
        Appointment appointment = serviceResponse.getAppointment();

        return RegisterNotificationByTokenResponse.builder()
                .appointmentId(appointment.getId())
                .appointmentName(appointment.getAppointmentName())
                .clientId(appointment.getClientId())
                .deviceToken(appointment.getDeviceToken())
                .notifications(serviceResponse.getNotifications())
                .build();
    }
}
