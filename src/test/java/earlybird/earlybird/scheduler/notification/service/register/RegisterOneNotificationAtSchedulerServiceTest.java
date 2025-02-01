package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.CreateTestAppointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepositoryStub;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.service.manager.NotificationSchedulerManagerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.Map;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStep.*;
import static org.assertj.core.api.Assertions.*;

class RegisterOneNotificationAtSchedulerServiceTest {
    private FcmNotificationRepository fcmNotificationRepository;
    private NotificationSchedulerManagerStub schedulerManager;

    @BeforeEach
    void setUp() {
        fcmNotificationRepository = new FcmNotificationRepositoryStub();
        schedulerManager = new NotificationSchedulerManagerStub();
    }

    @DisplayName("알림 1개를 스케줄러에 등록한다.")
    @Test
    void register() {
        // given
        RegisterOneNotificationAtSchedulerService service = new RegisterOneNotificationAtSchedulerService(
                fcmNotificationRepository, schedulerManager
        );

        Appointment appointment = CreateTestAppointment.create();

        Map<NotificationStep, Instant> notificationInfo = Map.of(APPOINTMENT_TIME, Instant.now().plusSeconds(10));

        // when
        service.register(appointment, notificationInfo);

        // then
        assertThat(appointment.getFcmNotifications()).hasSize(1);
        assertThat(schedulerManager.getNotificationIds()).hasSize(1);
    }

    @DisplayName("1개 이상의 알림 등록을 요청하면 예외가 발생한다.")
    @Test
    void registerMoreThanOne() {
        // given
        RegisterOneNotificationAtSchedulerService service = new RegisterOneNotificationAtSchedulerService(
                fcmNotificationRepository, schedulerManager
        );

        Appointment appointment = CreateTestAppointment.create();
        Map<NotificationStep, Instant> notificationInfo = Map.of(
                APPOINTMENT_TIME, Instant.now().plusSeconds(10),
                MOVING_TIME, Instant.now().plusSeconds(10)
        );

        // when // then
        assertThatThrownBy(() -> service.register(appointment, notificationInfo))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("등록 시점보다 과거의 알림은 등록되지 않는다.")
    @Test
    void test() {
        // given
        RegisterOneNotificationAtSchedulerService service = new RegisterOneNotificationAtSchedulerService(
                fcmNotificationRepository, schedulerManager
        );
        Appointment appointment = CreateTestAppointment.create();
        Map<NotificationStep, Instant> notificationInfo = Map.of(
                APPOINTMENT_TIME, Instant.now().minusSeconds(1)
        );

        // when
        service.register(appointment, notificationInfo);

        // then
        assertThat(appointment.getFcmNotifications()).hasSize(0);
        assertThat(schedulerManager.getNotificationIds()).hasSize(0);
    }
}