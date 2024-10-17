package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.UpdateFcmMessageServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UpdateNotificationService {

    private final AppointmentRepository appointmentRepository;
    private final RegisterNotificationAtSchedulerService registerNotificationAtSchedulerService;
    private final DeregisterNotificationAtSchedulerService deregisterNotificationAtSchedulerService;

    @Transactional
    public UpdateFcmMessageServiceResponse update(UpdateFcmMessageServiceRequest request) {

        Long appointmentId = request.getAppointmentId();
        String clientId = request.getClientId();
        Appointment appointment = findAppointmentBy(appointmentId, request.getClientId());

        deregisterNotificationAtSchedulerService.deregister(
                createDeregisterServiceRequest(appointmentId, appointment, clientId)
        );

        registerNotificationAtSchedulerService.registerFcmMessageForExistingAppointment(
                RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, appointment)
        );

        changeDeviceTokenIfChanged(appointment, request.getDeviceToken());
        changeAppointmentNameIfChanged(appointment, request.getAppointmentName());

        return UpdateFcmMessageServiceResponse.of(appointment);
    }

    private DeregisterFcmMessageAtSchedulerServiceRequest createDeregisterServiceRequest(
            Long appointmentId, Appointment appointment, String clientId
    ) {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .appointmentId(appointmentId)
                .optionalAppointment(Optional.of(appointment))
                .clientId(clientId)
                .build();
    }

    private Appointment findAppointmentBy(Long appointmentId, String clientId) {
        // TODO: 예외 추가
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(AppointmentNotFoundException::new);

        if (!appointment.getClientId().equals(clientId)) {
            throw new AppointmentNotFoundException();
        }

        return appointment;
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
