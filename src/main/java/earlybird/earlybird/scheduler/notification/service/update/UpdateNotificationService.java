package earlybird.earlybird.scheduler.notification.service.update;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.service.FindAppointmentService;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import earlybird.earlybird.scheduler.notification.service.NotificationInfoFactory;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterNotificationServiceRequestFactory;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateFcmMessageServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.response.UpdateFcmMessageServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UpdateNotificationService {

    private final RegisterNotificationService registerNotificationService;
    private final DeregisterNotificationService deregisterNotificationService;
    private final FindAppointmentService findAppointmentService;
    private final NotificationInfoFactory notificationInfoFactory;

    @Transactional
    public UpdateFcmMessageServiceResponse update(UpdateFcmMessageServiceRequest request) {

        Long appointmentId = request.getAppointmentId();
        String clientId = request.getClientId();
        Appointment appointment = findAppointmentService.findBy(appointmentId, request.getClientId());
        NotificationUpdateType updateType = request.getUpdateType();

        deregisterNotificationService.deregister(
                DeregisterNotificationServiceRequestFactory.create(appointmentId, clientId, updateType)
        );

        Map<NotificationStep, Instant> notificationInfo = notificationInfoFactory.createTargetTimeMap(
                request.getPreparationTime(), request.getMovingTime(), request.getAppointmentTime()
        );

        registerNotificationService.register(appointment, notificationInfo);

        changeDeviceTokenIfChanged(appointment, request.getDeviceToken());
        changeAppointmentNameIfChanged(appointment, request.getAppointmentName());

        return UpdateFcmMessageServiceResponse.of(appointment);
    }

    private void changeDeviceTokenIfChanged(Appointment appointment, String deviceToken) {
        if (!appointment.getDeviceToken().equals(deviceToken)) {
            appointment.changeDeviceToken(deviceToken);
        }
    }

    private void changeAppointmentNameIfChanged(Appointment appointment, String appointmentName) {
        if (!appointment.getAppointmentName().equals(appointmentName)) {
            appointment.changeAppointmentName(appointmentName);
        }
    }
}
