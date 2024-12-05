package earlybird.earlybird.scheduler.notification.service.register.repeating;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.RepeatingDay;
import earlybird.earlybird.appointment.domain.RepeatingDayRepository;
import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.register.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.PENDING;
import static earlybird.earlybird.scheduler.notification.domain.NotificationStep.APPOINTMENT_TIME;

// TODO: AWS에 맞게 로직 수정
@RequiredArgsConstructor
@Service
public class RegisterNotificationForRepeatingAppointmentService {

    private final RepeatingDayRepository repeatingDayRepository;
    private final RegisterNotificationAtSchedulerService registerService;

    @Transactional
    @Scheduled(cron = "0 0 0,23 * * ?", zone = "Asia/Seoul") // 매일 0시, 23시
    protected void registerEveryDay() {
        switch (LocalDateTimeUtil.getLocalDateTimeNow().getHour()) {
            case 0 -> registerAfterDay(0);
            case 23 -> registerAfterDay(1);
        }
    }

    private void registerAfterDay(int plusDays) {
        DayOfWeek afterTwoDayFromNow = LocalDateTimeUtil.getLocalDateTimeNow().plusDays(plusDays).getDayOfWeek();
        List<RepeatingDay> repeatingDays = repeatingDayRepository.findAllByDayOfWeek(afterTwoDayFromNow);
        repeatingDays.forEach(repeatingDay -> {
            Appointment appointment = repeatingDay.getAppointment();

            RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest registerRequest
                    = RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(appointment);

            boolean notificationIsNotRegistered = appointment.getFcmNotifications().stream()
                    .filter(notification -> notification.getNotificationStep().equals(APPOINTMENT_TIME))
                    .filter(notification -> isValidTargetTime(notification, registerRequest.getAppointmentTime()))
                    .noneMatch(notification -> notification.getStatus().equals(PENDING));

            if (notificationIsNotRegistered)
                registerService.registerFcmMessageForExistingAppointment(registerRequest);
        });
    }

    private boolean isValidTargetTime(FcmNotification notification, LocalDateTime appointmentTime) {
        return !notification.getTargetTime().isBefore(appointmentTime);
    }
}
