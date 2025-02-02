package earlybird.earlybird.scheduler.manager;

import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;

public interface NotificationSchedulerManager {
    void init();

    void add(AddNotificationToSchedulerServiceRequest request);

    void remove(Long notificationId);
}
