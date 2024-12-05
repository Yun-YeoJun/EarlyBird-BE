package earlybird.earlybird.appointment.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentUpdateType;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.service.NotificationInfoFactory;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.register.RegisterAllNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterNotificationServiceRequestFactory;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

import static earlybird.earlybird.appointment.domain.AppointmentUpdateType.MODIFY;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentService {

    private final DeregisterNotificationAtSchedulerService deregisterNotificationAtSchedulerService;
    private final FindAppointmentService findAppointmentService;
    private final DeregisterNotificationServiceRequestFactory deregisterServiceRequestFactory;
    private final RegisterAllNotificationAtSchedulerService registerService;
    private final NotificationInfoFactory factory;

    @Transactional
    public void update(UpdateAppointmentServiceRequest request) {

        Appointment appointment = findAppointmentService.findBy(request.getAppointmentId(), request.getClientId());
        AppointmentUpdateType updateType = request.getUpdateType();

        if (updateType.equals(MODIFY)) {
            modifyAppointment(appointment, request);
        }

        deregisterNotificationAtSchedulerService.deregister(
                deregisterServiceRequestFactory.create(request)
        );

        // TODO: create service 코드와 겹치는 코드 개선 방향 찾아보기
        LocalDateTime firstAppointmentTime = request.getFirstAppointmentTime();
        LocalDateTime movingTime = LocalDateTimeUtil.subtractDuration(firstAppointmentTime, request.getMovingDuration());
        LocalDateTime preparationTime = LocalDateTimeUtil.subtractDuration(movingTime, request.getPreparationDuration());

        Map<NotificationStep, Instant> notificationInfo =
                factory.createTargetTimeMap(preparationTime, movingTime, firstAppointmentTime);

        registerService.register(appointment, notificationInfo);
    }

    private void modifyAppointment(Appointment appointment, UpdateAppointmentServiceRequest request) {
        appointment.changeAppointmentName(request.getAppointmentName());
        appointment.changeDeviceToken(request.getDeviceToken());
        appointment.changePreparationDuration(request.getPreparationDuration());
        appointment.changeMovingDuration(request.getMovingDuration());
        appointment.changeAppointmentTime(request.getFirstAppointmentTime().toLocalTime());
        appointment.setRepeatingDays(request.getRepeatDayOfWeekList());
    }
}
