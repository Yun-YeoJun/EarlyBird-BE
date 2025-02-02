package earlybird.earlybird.scheduler.notification.service.register;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.CreateTestAppointment;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepositoryStub;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.service.manager.NotificationSchedulerManagerStub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

class RegisterAllNotificationAtSchedulerServiceTest {

    private FcmNotificationRepository fcmNotificationRepository;
    private NotificationSchedulerManagerStub schedulerManager;

    @BeforeEach
    void setUp() {
        fcmNotificationRepository = new FcmNotificationRepositoryStub();
        schedulerManager = new NotificationSchedulerManagerStub();
    }

    @DisplayName("등록 시점보다 미래의 다수 알림을 등록한다.")
    @Test
    void register() {
        // given
        RegisterAllNotificationAtSchedulerService service =
                new RegisterAllNotificationAtSchedulerService(
                        fcmNotificationRepository, schedulerManager);

        Appointment appointment = CreateTestAppointment.create();
        Map<NotificationStep, Instant> notificationInfo =
                Map.of(
                        NotificationStep.APPOINTMENT_TIME, Instant.now().plusSeconds(10),
                        NotificationStep.MOVING_TIME, Instant.now().plusSeconds(10));

        // when
        service.register(appointment, notificationInfo);

        // then
        assertThat(appointment.getFcmNotifications()).hasSize(notificationInfo.size());
        assertThat(schedulerManager.getNotificationIds()).hasSize(notificationInfo.size());
    }
}
