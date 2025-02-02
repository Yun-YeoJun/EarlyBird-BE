package earlybird.earlybird.scheduler.notification.service.register.request;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.common.LocalDateUtil;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateFcmMessageServiceRequest;
import java.time.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest {
  private String clientId;
  private String deviceToken;
  private LocalDateTime appointmentTime;
  private LocalDateTime preparationTime;
  private LocalDateTime movingTime;
  private Appointment appointment;

  @Builder
  private RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest(
      String clientId,
      String deviceToken,
      LocalDateTime appointmentTime,
      LocalDateTime preparationTime,
      LocalDateTime movingTime,
      Appointment appointment) {
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

  public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(
      RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request,
      Appointment appointment) {
    return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
        .clientId(request.getClientId())
        .deviceToken(request.getDeviceToken())
        .appointment(appointment)
        .preparationTime(request.getPreparationTime())
        .movingTime(request.getMovingTime())
        .appointmentTime(request.getAppointmentTime())
        .build();
  }

  public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(
      UpdateFcmMessageServiceRequest request, Appointment appointment) {
    return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
        .clientId(request.getClientId())
        .deviceToken(request.getDeviceToken())
        .appointment(appointment)
        .preparationTime(request.getPreparationTime())
        .movingTime(request.getMovingTime())
        .appointmentTime(request.getAppointmentTime())
        .build();
  }

  public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(
      Appointment appointment) {

    LocalDateTime appointmentTime =
        appointment.getAppointmentTime().atDate(LocalDateUtil.getLocalDateNow().plusDays(2));
    LocalDateTime movingTime =
        LocalDateTimeUtil.subtractDuration(appointmentTime, appointment.getMovingDuration());
    LocalDateTime preparationTime =
        LocalDateTimeUtil.subtractDuration(movingTime, appointment.getPreparationDuration());

    return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
        .clientId(appointment.getClientId())
        .deviceToken(appointment.getDeviceToken())
        .appointment(appointment)
        .preparationTime(preparationTime)
        .movingTime(movingTime)
        .appointmentTime(appointmentTime)
        .build();
  }

  public static RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest from(
      UpdateAppointmentServiceRequest request, Appointment appointment) {

    LocalDateTime firstAppointmentTime = request.getFirstAppointmentTime();
    LocalDateTime movingTime =
        LocalDateTimeUtil.subtractDuration(firstAppointmentTime, request.getMovingDuration());
    LocalDateTime preparationTime =
        LocalDateTimeUtil.subtractDuration(movingTime, request.getPreparationDuration());

    return RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.builder()
        .clientId(request.getClientId())
        .deviceToken(request.getDeviceToken())
        .appointment(appointment)
        .preparationTime(preparationTime)
        .movingTime(movingTime)
        .appointmentTime(firstAppointmentTime)
        .build();
  }
}
