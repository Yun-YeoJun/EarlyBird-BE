package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
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
    private final SchedulingTaskListService schedulingTaskListService;

    @Transactional
    public UpdateFcmMessageServiceResponse update(UpdateFcmMessageServiceRequest request) {
        checkTargetTime(request.getTargetTime());

        FcmNotification notification = fcmNotificationRepository.findById(request.getNotificationId())
                .orElseThrow(FcmNotificationNotFoundException::new);

        checkFcmDeviceTokenMismatch(request, notification);
        checkAlreadySentFcmNotification(notification);

        schedulingTaskListService.remove(notification.getUuid());
        notification.updateTargetTime(request.getTargetTime());
        schedulingTaskListService.add(createAddTaskRequest(request, notification));

        return UpdateFcmMessageServiceResponse.of(notification);
    }

    private void checkTargetTime(LocalDateTime targetTime) {
        if (targetTime.isBefore(LocalDateTime.now())) {
            throw new FcmMessageTimeBeforeNowException();
        }
    }

    private void checkAlreadySentFcmNotification(FcmNotification notification) {
        if (notification.getStatus().equals(FcmNotificationStatus.COMPLETED))
            throw new AlreadySentFcmNotificationException();
    }

    private void checkFcmDeviceTokenMismatch(UpdateFcmMessageServiceRequest request, FcmNotification notification) {
        if (!notification.getDeviceToken().equals(request.getDeviceToken()))
            throw new FcmDeviceTokenMismatchException();
    }

    private AddTaskToSchedulingTaskListServiceRequest createAddTaskRequest(UpdateFcmMessageServiceRequest request, FcmNotification notification) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .uuid(notification.getUuid())
                .targetTime(request.getTargetTimeInstant())
                .title(notification.getTitle())
                .body(notification.getBody())
                .deviceToken(notification.getDeviceToken())
                .build();
    }
}
