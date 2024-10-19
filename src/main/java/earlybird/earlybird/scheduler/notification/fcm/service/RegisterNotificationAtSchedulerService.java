package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

    private final AppointmentRepository appointmentRepository;
    private final FcmNotificationRepository fcmNotificationRepository;
    private final SchedulingTaskListService schedulingTaskListService;

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessageForNewAppointment(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
        Appointment newAppointment = createAppointmentBy(request);

        return registerFcmMessageForExistingAppointment(
                RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest.from(request, newAppointment)
        );
    }

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessageForExistingAppointment(RegisterFcmMessageForExistingAppointmentAtSchedulerServiceRequest request) {
        registerAll(
                request.getPreparationTimeInstant(),
                request.getMovingTimeInstant(),
                request.getAppointmentTimeInstant(),
                request.getAppointment(),
                request.getClientId(),
                request.getDeviceToken()
        );

        return RegisterFcmMessageAtSchedulerServiceResponse.of(request.getAppointment());
    }

    private Appointment createAppointmentBy(RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest request) {
        Appointment appointment = Appointment.builder()
                .appointmentName(request.getAppointmentName())
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    private void registerAll(Instant preparationTimeInstant, Instant movingTimeInstant, Instant appointmentTimeInstant, Appointment appointment, String clientId, String deviceToken) {
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
