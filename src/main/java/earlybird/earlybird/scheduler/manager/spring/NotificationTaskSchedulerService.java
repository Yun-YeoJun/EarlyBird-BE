package earlybird.earlybird.scheduler.manager.spring;

import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.messaging.MessagingService;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;
import earlybird.earlybird.messaging.request.SendMessageByTokenServiceRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.PENDING;

@Slf4j
@RequiredArgsConstructor
//@Service
public class NotificationTaskSchedulerService implements NotificationSchedulerManager {

    private final TaskScheduler taskScheduler;
    private final MessagingService messagingService;
    private final FcmNotificationRepository fcmNotificationRepository;

    private final ConcurrentHashMap<Long, ScheduledFuture<?>> notificationIdAndScheduleFutureMap
            = new ConcurrentHashMap<>();

    @PostConstruct
    @Override
    public void init() {
        fcmNotificationRepository.findAllByStatusIs(PENDING).stream()
                .filter(notification -> notification.getTargetTime()
                        .isAfter(LocalDateTimeUtil.getLocalDateTimeNow()))
                .map(AddNotificationToSchedulerServiceRequest::of)
                .forEach(this::add);
    }

    @Transactional
    @Override
    public void add(AddNotificationToSchedulerServiceRequest request) {
        Long notificationId = request.getNotificationId();
        Instant targetTime = request.getTargetTime();

        if (targetTime.isBefore(getNowInstant()))
            return;

        SendMessageByTokenServiceRequest sendRequest = SendMessageByTokenServiceRequest.from(request);

        ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
            messagingService.send(sendRequest);
            notificationIdAndScheduleFutureMap.remove(notificationId);
        }, targetTime);

        notificationIdAndScheduleFutureMap.put(notificationId, schedule);
    }

    private Instant getNowInstant() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant();
    }

    @Transactional
    @Override
    public void remove(Long notificationId) {
        ScheduledFuture<?> scheduledFuture = notificationIdAndScheduleFutureMap.get(notificationId);
        if (scheduledFuture == null) {
            return;
        }
        scheduledFuture.cancel(false);
        notificationIdAndScheduleFutureMap.remove(notificationId);
    }

    public boolean has(Long notificationId) {
        return notificationIdAndScheduleFutureMap.containsKey(notificationId);
    }
}
