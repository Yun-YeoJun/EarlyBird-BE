package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

    private final FcmNotificationRepository fcmNotificationRepository;
    private final AppointmentRepository appointmentRepository;
    private final SchedulingTaskListService schedulingTaskListService;

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessage(RegisterFcmMessageAtSchedulerServiceRequest request) {
        Instant appointmentTimeInstant = request.getAppointmentTimeInstant();
        Instant preparationTimeInstant = request.getPreparationTimeInstant();
        Instant movingTimeInstant = request.getMovingTimeInstant();

        Appointment appointment = createAppointmentBy(request);
        String deviceToken = request.getDeviceToken();
        String clientId = request.getClientId();

        registerAll(preparationTimeInstant, appointment, clientId, deviceToken, movingTimeInstant, appointmentTimeInstant);

        return RegisterFcmMessageAtSchedulerServiceResponse.of(appointment);
    }

    private Appointment createAppointmentBy(RegisterFcmMessageAtSchedulerServiceRequest request) {
        Appointment appointment = Appointment.builder()
                .appointmentName(request.getAppointmentName())
                .clientId(request.getClientId())
                .deviceToken(request.getDeviceToken())
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    private void registerAll(Instant preparationTimeInstant, Appointment appointment, String clientId, String deviceToken, Instant movingTimeInstant, Instant appointmentTimeInstant) {
        // 준비 시작 1시간 전
        register(
                preparationTimeInstant.minus(1, ChronoUnit.HOURS),
                appointment,
                appointment.getAppointmentName() + " 준비 시작까지 1시간 남았어요!",
                "오늘의 준비사항을 확인해봐요 \uD83D\uDE0A",
                clientId,
                deviceToken
        );

        // 준비 시작 5분 전
        register(
                preparationTimeInstant.minus(5, ChronoUnit.MINUTES),
                appointment,
                "5분 후에 준비 시작해야 해요!",
                "허겁지겁 준비하면 후회해요! \uD83E\uDEE2",
                clientId,
                deviceToken
        );

        // 준비 시작 시각
        register(
                preparationTimeInstant,
                appointment,
                "지금 준비 시작 안하면 늦어요 ❗\uFE0F❗\uFE0F❗\uFE0F",
                "같이 5초 세고, 시작해봐요!",
                clientId,
                deviceToken
        );

        // 이동 출발 10분 전
        register(
                movingTimeInstant.minus(10, ChronoUnit.MINUTES),
                appointment,
                "10분 후에 이동해야 안늦어요!",
                "교통정보를 미리 확인해보세요  \uD83D\uDEA5",
                clientId,
                deviceToken
        );

        // 이동 출발 시각
        register(
                movingTimeInstant,
                appointment,
                "지금 출발해야 안늦어요 ❗\uFE0F❗\uFE0F❗\uFE0F",
                "준비사항 다 체크하셨나요?",
                clientId,
                deviceToken
        );

        // 약속시각 5분 전
        register(
                appointmentTimeInstant.minus(5, ChronoUnit.MINUTES),
                appointment,
                "약속장소에 도착하셨나요?!",
                "도착하셨으면 확인버튼을 눌러주세요! \uD83E\uDD29",
                clientId,
                deviceToken
        );

        // 약속 시간
        register(
                appointmentTimeInstant,
                appointment,
                "1분 안에 확인버튼을 눌러주세요!!",
                "안 누르면 지각처리돼요!!! \uD83D\uDEAB\uD83D\uDEAB\uD83D\uDEAB",
                clientId,
                deviceToken
        );
    }


    private void register(Instant targetTime, Appointment appointment, String title, String body, String clientId, String deviceToken) {
        if (checkTimeBeforeNow(targetTime)) {
            return;
        }

        AddTaskToSchedulingTaskListServiceRequest addTaskRequest =
                createAddTaskRequest(targetTime, appointment, title, body, deviceToken);
        String notificationUuid = addTaskRequest.getUuid();

        schedulingTaskListService.add(addTaskRequest);

        FcmNotification notification = createFcmNotification(targetTime, appointment, title, body, clientId, deviceToken, notificationUuid);
        appointment.addFcmNotification(notification);
    }

    private FcmNotification createFcmNotification(Instant targetTime, Appointment appointment, String title, String body, String clientId, String deviceToken, String notificationUuid) {
        return FcmNotification.builder()
                .appointment(appointment)
                .targetTime(targetTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .title(title)
                .body(body)
                .uuid(notificationUuid)
                .build();
    }

    private boolean checkTimeBeforeNow(Instant time) {
        return (time.isBefore(Instant.now()));
    }

    private AddTaskToSchedulingTaskListServiceRequest createAddTaskRequest(
            Instant targetTime, Appointment appointment, String title, String body, String deviceToken) {
        return AddTaskToSchedulingTaskListServiceRequest.builder()
                .uuid(UUID.randomUUID().toString())
                .targetTime(targetTime)
                .appointment(appointment)
                .title(title)
                .deviceToken(deviceToken)
                .body(body)
                .build();
    }
}
