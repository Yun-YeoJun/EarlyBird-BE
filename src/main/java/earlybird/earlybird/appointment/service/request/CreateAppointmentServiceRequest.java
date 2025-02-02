package earlybird.earlybird.appointment.service.request;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CreateAppointmentServiceRequest {

  private final String appointmentName;
  private final String clientId;
  private final String deviceToken;
  private final Duration movingDuration;
  private final Duration preparationDuration;
  private final LocalDateTime firstAppointmentTime;
  private final List<DayOfWeek> repeatDayOfWeekList; // 월화수목금토일

  @Builder
  private CreateAppointmentServiceRequest(
      @NonNull String appointmentName,
      @NonNull String clientId,
      @NonNull String deviceToken,
      @NonNull LocalDateTime firstAppointmentTime,
      @NonNull Duration preparationDuration,
      @NonNull Duration movingDuration,
      @NonNull List<DayOfWeek> repeatDayOfWeekList) {

    this.appointmentName = appointmentName;
    this.clientId = clientId;
    this.deviceToken = deviceToken;
    this.firstAppointmentTime = firstAppointmentTime;
    this.preparationDuration = preparationDuration;
    this.movingDuration = movingDuration;
    this.repeatDayOfWeekList = repeatDayOfWeekList;
  }
}
