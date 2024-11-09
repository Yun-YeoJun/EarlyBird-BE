package earlybird.earlybird.log.arrive.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArriveOnTimeEventLogRepository extends JpaRepository<ArriveOnTimeEventLog, Long> {
}
