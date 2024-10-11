package earlybird.earlybird.scheduler.notification.fcm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmNotificationRepository extends JpaRepository<FcmNotification, Long> {

    Optional<FcmNotification> findByUuid(String uuid);
}
