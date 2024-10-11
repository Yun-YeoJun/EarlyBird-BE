package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;

@RequiredArgsConstructor
@Service
public class DeregisterNotificationAtSchedulerService {

    private final FcmNotificationRepository notificationRepository;

    public void deregister(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Long notificationId = request.getNotificationId();
        FcmNotification fcmNotification = notificationRepository.findById(notificationId)
                .orElseThrow(FcmNotificationNotFoundException::new);

        if (fcmNotification.getStatus().equals(COMPLETED)) {
            throw new AlreadySentFcmNotificationException();
        }

        if (!fcmNotification.getDeviceToken().equals(request.getDeviceToken())) {
            throw new FcmDeviceTokenMismatchException();
        }

        notificationRepository.deleteById(notificationId);
    }
}
