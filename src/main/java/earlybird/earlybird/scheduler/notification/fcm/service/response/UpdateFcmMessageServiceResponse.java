package earlybird.earlybird.scheduler.notification.fcm.service.response;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class UpdateFcmMessageServiceResponse {

    private final Appointment appointment;
    private final List<FcmNotification> notifications;

    public static UpdateFcmMessageServiceResponse of(Appointment appointment) {
        return UpdateFcmMessageServiceResponse.builder()
                .appointment(appointment)
                .notifications(appointment.getFcmNotifications())
                .build();
    }
}
