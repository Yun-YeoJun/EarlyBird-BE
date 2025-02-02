package earlybird.earlybird.appointment.service.request;

import earlybird.earlybird.appointment.domain.AppointmentUpdateType;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateAppointmentServiceRequest {

  private final Long appointmentId;
  private final String appointmentName;
  private final String clientId;
  private final String deviceToken;
  private final Duration preparationDuration;
  private final Duration movingDuration;
  private final LocalDateTime firstAppointmentTime;
  private final AppointmentUpdateType updateType;
  private final List<DayOfWeek> repeatDayOfWeekList;

  @Builder
  private UpdateAppointmentServiceRequest(
      Long appointmentId,
      String appointmentName,
      String clientId,
      String deviceToken,
      Duration preparationDuration,
      Duration movingDuration,
      LocalDateTime firstAppointmentTime,
      AppointmentUpdateType updateType,
      List<DayOfWeek> repeatDayOfWeekList) {
    this.appointmentId = appointmentId;
    this.appointmentName = appointmentName;
    this.clientId = clientId;
    this.deviceToken = deviceToken;
    this.preparationDuration = preparationDuration;
    this.movingDuration = movingDuration;
    this.firstAppointmentTime = firstAppointmentTime;
    this.updateType = updateType;
    this.repeatDayOfWeekList = repeatDayOfWeekList;
  }
}
