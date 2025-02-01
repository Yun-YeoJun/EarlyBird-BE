package earlybird.earlybird.appointment.domain;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class CreateTestAppointment {

    public static Appointment create() {
        return Appointment.builder()
                .appointmentTime(LocalTime.now())
                .repeatingDayOfWeeks(new ArrayList<>())
                .movingDuration(Duration.ofMinutes(10))
                .preparationDuration(Duration.ofMinutes(10))
                .deviceToken("deviceToken")
                .clientId("clientId")
                .appointmentName("appointmentName")
                .build();
    }
}
