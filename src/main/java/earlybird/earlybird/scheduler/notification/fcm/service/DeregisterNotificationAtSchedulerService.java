package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;

@RequiredArgsConstructor
@Service
public class DeregisterNotificationAtSchedulerService {

    private final FcmNotificationRepository notificationRepository;
    private final SchedulingTaskListService schedulingTaskListService;

    @Transactional
    public void deregister(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        Long notificationId = request.getNotificationId();
        FcmNotification fcmNotification = notificationRepository.findById(notificationId)
                .orElseThrow(FcmNotificationNotFoundException::new);

        checkNotificationIsAlreadySent(fcmNotification);
        checkDeviceTokenMismatch(request, fcmNotification);

        schedulingTaskListService.remove(fcmNotification.getUuid());
        notificationRepository.delete(fcmNotification);
    }

    private void checkDeviceTokenMismatch(DeregisterFcmMessageAtSchedulerServiceRequest request, FcmNotification fcmNotification) {
        if (!fcmNotification.getDeviceToken().equals(request.getDeviceToken())) {
            throw new FcmDeviceTokenMismatchException();
        }
    }

    private void checkNotificationIsAlreadySent(FcmNotification fcmNotification) {
        if (fcmNotification.getStatus().equals(COMPLETED)) {
            throw new AlreadySentFcmNotificationException();
        }
    }
}
