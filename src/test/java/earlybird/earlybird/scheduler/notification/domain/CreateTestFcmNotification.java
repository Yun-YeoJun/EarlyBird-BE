package earlybird.earlybird.scheduler.notification.domain;

import earlybird.earlybird.appointment.domain.CreateTestAppointment;
import java.time.LocalDateTime;

public class CreateTestFcmNotification {

  public static FcmNotification create() {
    return FcmNotification.builder()
        .appointment(CreateTestAppointment.create())
        .notificationStep(NotificationStep.APPOINTMENT_TIME)
        .targetTime(LocalDateTime.now())
        .build();
  }
}
