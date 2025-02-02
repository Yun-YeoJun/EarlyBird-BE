package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.common.InstantUtil;
import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class RegisterOneNotificationAtSchedulerService implements RegisterNotificationService {

  private final FcmNotificationRepository notificationRepository;
  private final NotificationSchedulerManager notificationSchedulerManager;

  @Override
  @Transactional
  public void register(
      Appointment appointment, Map<NotificationStep, Instant> notificationStepAndTargetTime) {

    if (notificationStepAndTargetTime.size() != 1)
      throw new IllegalArgumentException("1개의 알림만 등록할 수 있습니다");

    NotificationStep notificationStep = notificationStepAndTargetTime.keySet().iterator().next();
    Instant targetTime = notificationStepAndTargetTime.get(notificationStep);

    if (InstantUtil.checkTimeBeforeNow(targetTime)) return;

    String deviceToken = appointment.getDeviceToken();

    FcmNotification notification = createNotification(notificationStep, targetTime, appointment);

    AddNotificationToSchedulerServiceRequest addTaskRequest =
        createAddTaskRequest(notificationStep, targetTime, appointment, notification, deviceToken);

    notificationSchedulerManager.add(addTaskRequest);
  }

  private FcmNotification createNotification(
      NotificationStep notificationStep, Instant targetTime, Appointment appointment) {
    FcmNotification notification =
        FcmNotification.builder()
            .appointment(appointment)
            .targetTime(targetTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
            .notificationStep(notificationStep)
            .build();

    appointment.addFcmNotification(notification);
    return notificationRepository.save(notification);
  }

  private AddNotificationToSchedulerServiceRequest createAddTaskRequest(
      NotificationStep notificationStep,
      Instant targetTime,
      Appointment appointment,
      FcmNotification notification,
      String deviceToken) {
    return AddNotificationToSchedulerServiceRequest.builder()
        .notificationId(notification.getId())
        .targetTime(targetTime)
        .appointment(appointment)
        .notificationStep(notificationStep)
        .deviceToken(deviceToken)
        .build();
  }
}
