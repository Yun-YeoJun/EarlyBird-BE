package earlybird.earlybird.scheduler.notification.service.update;

import earlybird.earlybird.error.exception.NotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateNotificationStatusService {

  private final FcmNotificationRepository notificationRepository;

  @Transactional
  public void update(Long notificationId, Boolean sendSuccess) {
    FcmNotification notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(NotificationNotFoundException::new);
    if (sendSuccess) notification.onSendToFcmSuccess();
    else notification.onSendToFcmFailure();
  }
}
