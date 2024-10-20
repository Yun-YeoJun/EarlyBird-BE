package earlybird.earlybird.scheduler.notification.fcm.domain;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Component
class Transaction {

    @Transactional
    public void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

@Import(Transaction.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class FcmNotificationRepositoryTest {

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private Transaction transaction;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
        appointmentRepository.deleteAllInBatch();
    }

    @DisplayName("ID와 전송 상태로 FCM 메시지를 조회한다.")
    @Test
    void findByIdAndStatus() {
        // given
        Appointment appointment = createAppointment();
        FcmNotification notification = createNotification(appointment);
        appointment.addFcmNotification(notification);
        appointmentRepository.save(appointment);
        FcmNotification savedNotification = fcmNotificationRepository.save(notification);


        transaction.run(() -> {
            // when
            FcmNotification result = fcmNotificationRepository.findByIdAndStatusForUpdate(savedNotification.getId(), savedNotification.getStatus()).get();

            // then
            assertThat(result.getId()).isEqualTo(savedNotification.getId());
            assertThat(result.getStatus()).isEqualTo(savedNotification.getStatus());
        });
    }

    @DisplayName("findByIdAndStatusForUpdate 메서드를 실행하면 X-lock 락이 동작한다.")
    @Test
    void pessimisticWriteLock() {
        // given
        Appointment appointment = createAppointment();
        FcmNotification notification = createNotification(appointment);
        appointment.addFcmNotification(notification);
        appointmentRepository.save(appointment);
        FcmNotification savedNotification = fcmNotificationRepository.save(notification);

        // when
        CompletableFuture<Void> transaction1 = CompletableFuture.runAsync(() -> transaction.run(() -> {
            FcmNotification fcmNotification = fcmNotificationRepository.findByIdAndStatusForUpdate(savedNotification.getId(), savedNotification.getStatus()).get();
            threadSleep(5000);
            fcmNotification.updateStatusTo(NotificationStatus.CANCELLED);
        }));

        threadSleep(500);

        CompletableFuture<Void> transaction2 = CompletableFuture.runAsync(() -> {
            transaction.run(() -> {
                FcmNotification fcmNotification = fcmNotificationRepository.findById(savedNotification.getId()).get();
                fcmNotification.updateStatusTo(NotificationStatus.COMPLETED);
            });
        });

        transaction1.join();
        transaction2.join();

        // then
        FcmNotification fcmNotification = fcmNotificationRepository.findById(savedNotification.getId()).get();
        assertThat(fcmNotification.getStatus()).isEqualTo(NotificationStatus.COMPLETED);
    }


    private void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Appointment createAppointment() {
        return appointmentRepository.save(Appointment.builder()
                .appointmentName("appointmentName")
                .deviceToken("deviceToken")
                .clientId("clientId")
                .build());
    }

    private FcmNotification createNotification(Appointment appointment) {
        return FcmNotification.builder()
                .appointment(appointment)
                .targetTime(LocalDateTime.of(2024, 10, 11, 0, 0))
                .notificationStep(NotificationStep.APPOINTMENT_TIME)
                .build();
    }
}



