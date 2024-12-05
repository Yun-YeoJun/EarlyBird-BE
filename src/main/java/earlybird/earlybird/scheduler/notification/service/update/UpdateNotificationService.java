package earlybird.earlybird.scheduler.notification.service.update;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.appointment.service.FindAppointmentService;
import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterNotificationServiceRequestFactory;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateFcmMessageServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.response.UpdateFcmMessageServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateNotificationService {

    private final RegisterNotificationAtSchedulerService registerNotificationAtSchedulerService;
    private final DeregisterNotificationAtSchedulerService deregisterNotificationAtSchedulerService;
    private final DeregisterNotificationServiceRequestFactory deregisterServiceRequestFactory;
    private final FindAppointmentService findAppointmentService;

    @Transactional
    public UpdateFcmMessageServiceResponse update(UpdateFcmMessageServiceRequest request) {

        Long appointmentId = request.getAppointmentId();
        String clientId = request.getClientId();
        Appointment appointment = findAppointmentService.findBy(appointmentId, request.getClientId());
        NotificationUpdateType updateType = request.getUpdateType();

        deregisterNotificationAtSchedulerService.deregister(
                deregisterServiceRequestFactory.create(appointmentId, clientId, updateType)
        );

        registerNotificationAtSchedulerService.registerFcmMessageForExistingAppointment(
                RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, appointment)
        );

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
