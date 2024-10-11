package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.UpdateFcmMessageServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UpdateNotificationService {

    private final FcmNotificationRepository fcmNotificationRepository;

    @Transactional
    public UpdateFcmMessageServiceResponse update(UpdateFcmMessageServiceRequest request) {
        if (request.getTargetTime().isBefore(LocalDateTime.now())) {
            throw new FcmMessageTimeBeforeNowException();
        }

        FcmNotification notification = fcmNotificationRepository.findById(request.getNotificationId())
                .orElseThrow(FcmNotificationNotFoundException::new);

        if (!notification.getDeviceToken().equals(request.getDeviceToken()))
            throw new FcmDeviceTokenMismatchException();

        if (notification.getStatus().equals(FcmNotificationStatus.COMPLETED))
            throw new AlreadySentFcmNotificationException();

        notification.updateTargetTime(request.getTargetTime());

        return UpdateFcmMessageServiceResponse.of(notification);
    }
}
