package earlybird.earlybird.appointment.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.appointment.service.request.CreateAppointmentServiceRequest;
import earlybird.earlybird.appointment.service.response.CreateAppointmentServiceResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final RegisterNotificationAtSchedulerService registerNotificationAtSchedulerService;

    @Transactional
    public CreateAppointmentServiceResponse create(CreateAppointmentServiceRequest request) {

        Appointment appointment = createAppointmentInstance(request);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest registerFcmServiceRequest
                = RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, savedAppointment);

        registerNotificationAtSchedulerService.registerFcmMessageForExistingAppointment(registerFcmServiceRequest);

        return CreateAppointmentServiceResponse.builder()
                .createdAppointmentId(savedAppointment.getId())
                .build();
    }

    private Appointment createAppointmentInstance(CreateAppointmentServiceRequest request) {
        return Appointment.builder()
                .appointmentName(request.getAppointmentName())
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .repeatingDayOfWeeks(request.getRepeatDayOfWeekList())
                .appointmentTime(request.getFirstAppointmentTime().toLocalTime())
                .preparationDuration(request.getPreparationDuration())
                .movingDuration(request.getMovingDuration())
                .build();
    }
}
