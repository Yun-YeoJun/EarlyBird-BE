package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.PENDING;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulingTaskListService {

    private final TaskScheduler taskScheduler;
    private final SendMessageToFcmService sendMessageToFcmService;
    private final FcmNotificationRepository fcmNotificationRepository;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> notificationUuidAndScheduleFutureMap
            = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        fcmNotificationRepository.findAll().stream()
                .filter(notification -> notification.getStatus().equals(PENDING))
                .filter(notification -> notification.getTargetTime().isAfter(LocalDateTime.now()))
                .map(AddTaskToSchedulingTaskListServiceRequest::of)
                .forEach(this::add);
    }

    @Transactional
    public void add(AddTaskToSchedulingTaskListServiceRequest request) {
        String uuid = request.getUuid();
        Instant targetTime = request.getTargetTime();
        SendMessageByTokenServiceRequest sendRequest = SendMessageByTokenServiceRequest.from(request);

        ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
            try {
                sendMessageToFcmService.sendMessageByToken(sendRequest);
                notificationUuidAndScheduleFutureMap.remove(uuid);
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
            }
        }, targetTime);

        notificationUuidAndScheduleFutureMap.put(uuid, schedule);
    }

    @Transactional
    public void remove(String uuid) {
        ScheduledFuture<?> scheduledFuture = notificationUuidAndScheduleFutureMap.get(uuid);
        if (scheduledFuture == null) {
            throw new FcmNotificationNotFoundException();
        }
        scheduledFuture.cancel(false);
        notificationUuidAndScheduleFutureMap.remove(uuid);
    }

    public boolean has(String uuid) {
        return notificationUuidAndScheduleFutureMap.containsKey(uuid);
    }
}
