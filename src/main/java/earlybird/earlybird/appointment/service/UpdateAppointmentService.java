package earlybird.earlybird.appointment.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.appointment.domain.AppointmentUpdateType;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.service.DeregisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.fcm.service.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static earlybird.earlybird.appointment.domain.AppointmentUpdateType.MODIFY;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentService {

    private final DeregisterNotificationAtSchedulerService deregisterNotificationAtSchedulerService;
    private final RegisterNotificationAtSchedulerService registerNotificationAtSchedulerService;
    private final FindAppointmentService findAppointmentService;

    @Transactional
    public void update(UpdateAppointmentServiceRequest request) {

        Appointment appointment = findAppointmentService.findBy(request.getAppointmentId(), request.getClientId());
        AppointmentUpdateType updateType = request.getUpdateType();

        if (updateType.equals(MODIFY)) {
            modifyAppointment(appointment, request);
        }

        deregisterNotificationAtSchedulerService.deregister(
                DeregisterFcmMessageAtSchedulerServiceRequest.from(request)
        );

        registerNotificationAtSchedulerService.registerFcmMessageForExistingAppointment(
                RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, appointment)
        );
    }

    private void modifyAppointment(Appointment appointment, UpdateAppointmentServiceRequest request) {
        appointment.changeAppointmentName(request.getAppointmentName());
        appointment.changeDeviceToken(request.getDeviceToken());
        appointment.changePreparationDuration(request.getPreparationDuration());
        appointment.changeMovingDuration(request.getMovingDuration());
        appointment.changeAppointmentTime(request.getFirstAppointmentTime().toLocalTime());
        appointment.setRepeatingDays(request.getRepeatDayOfWeekList());
    }
}
