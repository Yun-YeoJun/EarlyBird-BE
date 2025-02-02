package earlybird.earlybird.scheduler.notification.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FcmNotificationRepository extends JpaRepository<FcmNotification, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT n FROM FcmNotification n WHERE n.id = :id AND n.status = :status")
  Optional<FcmNotification> findByIdAndStatusForUpdate(
      @Param("id") Long id, @Param("status") NotificationStatus status);

  List<FcmNotification> findAllByStatusIs(NotificationStatus status);
}
