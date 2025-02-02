package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.service.NotificationInfoFactory;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.service.register.response.RegisterNotificationServiceResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Deprecated
@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

  private final AppointmentRepository appointmentRepository;
  private final RegisterAllNotificationAtSchedulerService registerAllNotificationAtSchedulerService;
  private final NotificationInfoFactory notificationInfoFactory;

  @Deprecated
  @Transactional
  public RegisterNotificationServiceResponse registerFcmMessageForNewAppointment(
      RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
    Appointment newAppointment = createAppointmentBy(request);

    return registerFcmMessageForExistingAppointment(
        RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(
            request, newAppointment));
  }

  @Transactional
  public RegisterNotificationServiceResponse registerFcmMessageForExistingAppointment(
      RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest request) {
    Appointment appointment = request.getAppointment();

    Map<NotificationStep, Instant> targetTimeMap =
        notificationInfoFactory.createTargetTimeMap(
            request.getPreparationTimeInstant(),
            request.getMovingTimeInstant(),
            request.getAppointmentTimeInstant());

    registerAllNotificationAtSchedulerService.register(appointment, targetTimeMap);

    return RegisterNotificationServiceResponse.builder()
        .appointment(appointment)
        .notifications(appointment.getFcmNotifications())
        .build();
  }

  private Appointment createAppointmentBy(
      RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
    Appointment appointment =
        Appointment.builder()
            .appointmentName(request.getAppointmentName())
            .clientId(request.getClientId())
            .deviceToken(request.getDeviceToken())
            .appointmentTime(request.getAppointmentTime().toLocalTime())
            .movingDuration(Duration.between(request.getMovingTime(), request.getAppointmentTime()))
            .preparationDuration(
                Duration.between(request.getPreparationTime(), request.getMovingTime()))
            .repeatingDayOfWeeks(new ArrayList<>())
            .build();

    return appointmentRepository.save(appointment);
  }
}
