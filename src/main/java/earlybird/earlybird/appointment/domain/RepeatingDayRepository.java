package earlybird.earlybird.appointment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface RepeatingDayRepository extends JpaRepository<RepeatingDay, Long> {

    List<RepeatingDay> findAllByDayOfWeek(DayOfWeek dayOfWeek);
}
