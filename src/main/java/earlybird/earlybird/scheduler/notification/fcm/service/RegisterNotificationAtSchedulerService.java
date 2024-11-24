package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.appointment.domain.RepeatingDay;
import earlybird.earlybird.appointment.domain.RepeatingDayRepository;
import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.PENDING;
import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

    private final AppointmentRepository appointmentRepository;
    private final FcmNotificationRepository fcmNotificationRepository;
    private final SchedulingTaskListService schedulingTaskListService;
    private final RepeatingDayRepository repeatingDayRepository;

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessageForNewAppointment(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
        Appointment newAppointment = createAppointmentBy(request);

        return registerFcmMessageForExistingAppointment(
                RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, newAppointment)
        );
    }

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessageForExistingAppointment(RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest request) {
        registerAll(request);
        return RegisterFcmMessageAtSchedulerServiceResponse.of(request.getAppointment());
    }

    @Transactional
    @Scheduled(cron = "0 0 23 * * ?", zone = "Asia/Seoul") // 매일 23시
    protected void registerFcmMessageForRepeatingAppointment() {
        DayOfWeek afterTwoDayFromNow = LocalDateTimeUtil.getLocalDateTimeNow().plusDays(2).getDayOfWeek();
        List<RepeatingDay> repeatingDays = repeatingDayRepository.findAllByDayOfWeek(afterTwoDayFromNow);
        repeatingDays.forEach(repeatingDay -> {
            Appointment appointment = repeatingDay.getAppointment();

            RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest registerRequest
                    = RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(appointment);

            boolean notificationIsNotRegistered = appointment.getFcmNotifications().stream()
                    .filter(notification -> notification.getNotificationStep().equals(APPOINTMENT_TIME))
                    .filter(notification -> !notification.getTargetTime().isBefore(registerRequest.getAppointmentTime()))
                    .noneMatch(notification -> notification.getStatus().equals(PENDING));

            if (notificationIsNotRegistered)
                registerFcmMessageForExistingAppointment(registerRequest);
        });
    }

    private Appointment createAppointmentBy(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
        Appointment appointment = buildAppointmentFrom(request);
        appointmentRepository.save(appointment);
        return appointment;
    }

    private Appointment buildAppointmentFrom(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
        return Appointment.builder()
                .appointmentName(request.getAppointmentName())
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .appointmentTime(request.getAppointmentTime().toLocalTime())
                .movingDuration(Duration.between(request.getMovingTime(), request.getAppointmentTime()))
                .preparationDuration(Duration.between(request.getPreparationTime(), request.getMovingTime()))
                .repeatingDayOfWeeks(new ArrayList<>())
                .build();
    }

    private void registerAll(RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest request) {

        Instant preparationTimeInstant = request.getPreparationTimeInstant();
        Instant movingTimeInstant = request.getMovingTimeInstant();
        Instant appointmentTimeInstant = request.getAppointmentTimeInstant();
        Appointment appointment = request.getAppointment();
        String clientId = request.getClientId();
        String deviceToken = request.getDeviceToken();

        // 준비 시작 1시간 전
        register(
                preparationTimeInstant.minus(1, ChronoUnit.HOURS),
                appointment,
                ONE_HOUR_BEFORE_PREPARATION_TIME,
                clientId,
                deviceToken
        );

        // 준비 시작 5분 전
        register(
                preparationTimeInstant.minus(5, ChronoUnit.MINUTES),
                appointment,
                FIVE_MINUTES_BEFORE_PREPARATION_TIME,
                clientId,
                deviceToken
        );

        // 준비 시작 시각
        register(
                preparationTimeInstant,
                appointment,
                PREPARATION_TIME,
                clientId,
                deviceToken
        );

        // 이동 출발 10분 전
        register(
                movingTimeInstant.minus(10, ChronoUnit.MINUTES),
                appointment,
                TEN_MINUTES_BEFORE_MOVING_TIME,
                clientId,
                deviceToken
        );

        // 이동 출발 시각
        register(
                movingTimeInstant,
                appointment,
                MOVING_TIME,
                clientId,
                deviceToken
        );

        // 약속시각 5분 전
        register(
                appointmentTimeInstant.minus(5, ChronoUnit.MINUTES),
                appointment,
                FIVE_MINUTES_BEFORE_APPOINTMENT_TIME,
                clientId,
                deviceToken
        );

        // 약속 시간
        register(
                appointmentTimeInstant,
                appointment,
                APPOINTMENT_TIME,
                clientId,
                deviceToken
        );
    }


    private void register(Instant targetTime, Appointment appointment, NotificationStep notificationStep, String clientId, String deviceToken) {
        if (checkTimeBeforeNow(targetTime)) {
            return;
        }

        FcmNotification notification = createFcmNotification(targetTime, appointment, notificationStep);
        appointment.addFcmNotification(notification);
        fcmNotificationRepository.save(notification);

        AddTaskToSchedulingTaskListServiceRequest addTaskRequest =
                createAddTaskRequest(notification.getId(), targetTime, appointment, notificationStep, deviceToken);

        schedulingTaskListService.add(addTaskRequest);
    }

    private boolean checkTimeBeforeNow(Instant time) {
        return (time.isBefore(Instant.now()));
    }

    private AddTaskToSchedulingTaskListServiceRequest createAddTaskRequest(
            Long notificationId, Instant targetTime, Appointment appointment, NotificationStep notificationStep, String deviceToken) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .notificationId(notificationId)
                .targetTime(targetTime)
                .appointment(appointment)
                .notificationStep(notificationStep)
                .deviceToken(deviceToken)
                .build();
    }

    private FcmNotification createFcmNotification(Instant targetTime, Appointment appointment, NotificationStep notificationStep) {
        return FcmNotification.builder()
                .appointment(appointment)
                .targetTime(targetTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .notificationStep(notificationStep)
                .build();
    }
}
