package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.SendMessageByTokenServiceResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@SpringBootTest
class SendMessageToFcmServiceTest {

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SendMessageToFcmService sendMessageToFcmService;

    @MockBean
    private FirebaseMessagingService firebaseMessagingService;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
        appointmentRepository.deleteAllInBatch();
    }

    @DisplayName("FCM 으로 메시지를 보내고 전송 정보를 DB에 업데이트한다.")
    @Test
    void sendMessageByToken() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        // given
        String title = "title";
        String body = "body";
        String uuid = "uuid";
        String deviceToken = "deviceToken";
        LocalDateTime targetTime = LocalDateTime.of(2024, 10, 9, 0, 0);

        SendMessageByTokenServiceRequest request = createRequest(title, body, uuid, deviceToken);

        Appointment appointment = createAppointment();
        FcmNotification fcmNotification = createFcmNotification(appointment, title, body, uuid, deviceToken, targetTime);
        appointment.addFcmNotification(fcmNotification);
        appointmentRepository.save(appointment);

        String fcmMessageId = "fcm-message-id";
        doReturn(fcmMessageId).when(firebaseMessagingService).send(request);

        // when
        SendMessageByTokenServiceResponse response = sendMessageToFcmService.sendMessageByToken(request).get();

        // then
        assertThat(fcmNotificationRepository.findByUuid(uuid)).isPresent();
        assertThat(fcmNotificationRepository.findByUuid(uuid).get()).extracting(
                FcmNotification::getUuid, FcmNotification::getTitle, FcmNotification::getBody,
                 FcmNotification::getTargetTime, FcmNotification::getStatus, FcmNotification::getFcmMessageId)
                .contains(
                        uuid, title, body, targetTime, COMPLETED, fcmMessageId
                );

        assertThat(fcmNotificationRepository.findByUuid(uuid).get().getAppointment())
                .isNotNull()
                .extracting(
                        Appointment::getId, Appointment::getAppointmentName, Appointment::getClientId, Appointment::getDeviceToken)
                .contains(
                        appointment.getId(), appointment.getAppointmentName(), appointment.getClientId(), appointment.getDeviceToken()
                );

        assertThat(appointmentRepository.findById(appointment.getId())).isNotEmpty();
    }

    private Appointment createAppointment() {
        return appointmentRepository.save(Appointment.builder()
                        .appointmentName("appointmentName")
                        .clientId("clientId")
                        .deviceToken("deviceToken")
                        .build());
    }

    private FcmNotification createFcmNotification(Appointment appointment, String title, String body, String uuid, String deviceToken, LocalDateTime targetTime) {
        return FcmNotification.builder()
                .appointment(appointment)
                .title(title)
                .body(body)
                .uuid(uuid)
                .targetTime(targetTime)
                .build();
    }

    private SendMessageByTokenServiceRequest createRequest(String title, String body, String uuid, String deviceToken) {
        return SendMessageByTokenServiceRequest.builder()
                .title(title)
                .body(body)
                .uuid(uuid)
                .deviceToken(deviceToken)
                .build();
    }

}