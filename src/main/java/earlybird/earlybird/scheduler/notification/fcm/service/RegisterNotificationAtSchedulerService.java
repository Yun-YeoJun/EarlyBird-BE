package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

    private final FcmNotificationRepository fcmNotificationRepository;
    private final SchedulingTaskListService schedulingTaskListService;

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessage(RegisterFcmMessageAtSchedulerServiceRequest request) {
        Instant targetTime = request.getTargetTimeInstant();
        checkTargetTime(targetTime);

        schedulingTaskListService.add(createAddTaskRequest(request, targetTime));

        FcmNotification savedNotification = fcmNotificationRepository.save(request.toFcmNotification());
        return RegisterFcmMessageAtSchedulerServiceResponse.of(savedNotification);
    }

    private void checkTargetTime(Instant targetTime) {
        if (targetTime.isBefore(Instant.now())) {
            throw new FcmMessageTimeBeforeNowException();
        }
    }

    private AddTaskToSchedulingTaskListServiceRequest createAddTaskRequest(RegisterFcmMessageAtSchedulerServiceRequest request, Instant targetTime) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .uuid(request.getUuid())
                .targetTime(targetTime)
                .title(request.getTitle())
                .deviceToken(request.getDeviceToken())
                .body(request.getBody())
                .build();
    }
}
