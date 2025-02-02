package earlybird.earlybird.appointment.domain;

import java.time.DayOfWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatingDayRepository extends JpaRepository<RepeatingDay, Long> {

  List<RepeatingDay> findAllByDayOfWeek(DayOfWeek dayOfWeek);
}
