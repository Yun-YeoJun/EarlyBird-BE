package earlybird.earlybird.scheduler.notification.service.manager;

import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;

import java.util.ArrayList;
import java.util.List;

public class NotificationSchedulerManagerStub implements NotificationSchedulerManager {
    private final List<Long> notificationIds = new ArrayList<Long>();

    @Override
    public void init() {
        return;
    }

    @Override
    public void add(AddNotificationToSchedulerServiceRequest request) {
        this.notificationIds.add(request.getNotificationId());
    }

    @Override
    public void remove(Long notificationId) {
        this.notificationIds.remove(notificationId);
    }

    public List<Long> getNotificationIds() {
        return notificationIds;
    }
}
