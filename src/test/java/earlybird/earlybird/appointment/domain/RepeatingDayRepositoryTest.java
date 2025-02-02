package earlybird.earlybird.appointment.domain;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RepeatingDayRepositoryTest {

  @Autowired private AppointmentRepository appointmentRepository;

  @Autowired private RepeatingDayRepository repeatingDayRepository;

  @DisplayName("")
  @Test
  void test() {
    // given

    Appointment appointment =
        Appointment.builder()
            .appointmentTime(LocalTime.now())
            .appointmentName("name")
            .deviceToken("token")
            .repeatingDayOfWeeks(List.of(DayOfWeek.MONDAY))
            .preparationDuration(Duration.ZERO)
            .movingDuration(Duration.ZERO)
            .clientId("clientId")
            .build();
    appointmentRepository.save(appointment);
    repeatingDayRepository.findAllByDayOfWeek(DayOfWeek.MONDAY);

    // when
    repeatingDayRepository.findAll();
    System.out.println("dd");

    // then

  }
}
