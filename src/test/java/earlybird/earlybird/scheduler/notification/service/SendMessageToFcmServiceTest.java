//package earlybird.earlybird.scheduler.notification.fcm.service;
//
//import com.google.firebase.messaging.FirebaseMessagingException;
//import earlybird.earlybird.appointment.domain.Appointment;
//import earlybird.earlybird.appointment.domain.AppointmentRepository;
//import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
//import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
//import earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep;
//import earlybird.earlybird.messaging.request.SendMessageByTokenServiceRequest;
//import org.awaitility.Awaitility;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.COMPLETED;
//import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStep.APPOINTMENT_TIME;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.doReturn;
//
//@ActiveProfiles("test")
//@SpringBootTest
//class SendMessageToFcmServiceTest {
//
//    @Autowired
//    private FcmNotificationRepository fcmNotificationRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private SendMessageToFcmService sendMessageToFcmService;
//
//    @MockBean
//    private MessagingService messagingService;
//
//    @AfterEach
//    void tearDown() {
//        fcmNotificationRepository.deleteAllInBatch();
//        appointmentRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("FCM 으로 메시지를 보내고 전송 정보를 DB에 업데이트한다.")
//    @Test
//    void sendMessageByToken() throws FirebaseMessagingException, ExecutionException, InterruptedException {
//        // given
//        String title = "title";
//        String body = "body";
//        String uuid = "uuid";
//        String deviceToken = "deviceToken";
//        LocalDateTime targetTime = LocalDateTime.of(2024, 10, 9, 0, 0);
//
//
//        Appointment appointment = createAppointment();
//        FcmNotification fcmNotification = createFcmNotification(appointment, targetTime, APPOINTMENT_TIME);
//        appointment.addFcmNotification(fcmNotification);
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//        FcmNotification savedNotification = fcmNotificationRepository.save(fcmNotification);
//
//        SendMessageByTokenServiceRequest request = createRequest(title, body, deviceToken, savedNotification.getId());
//
//        String fcmMessageId = "fcm-message-id";
//        doReturn(fcmMessageId).when(messagingService).send(request);
//
//        // when
//        sendMessageToFcmService.sendMessageByToken(request);
//
//        // then
//        Awaitility.await()
//                .atMost(5, TimeUnit.SECONDS)
//                .until(() -> fcmNotificationRepository.findById(savedNotification.getId()).get().getStatus().equals(COMPLETED));
//
//        Optional<FcmNotification> optional = fcmNotificationRepository.findById(savedNotification.getId());
//        assertThat(optional).isPresent();
//        assertThat(optional.get().getStatus()).isEqualTo(COMPLETED);
//        assertThat(optional.get().getSentTime()).isNotNull();
//    }
//
//    private Appointment createAppointment() {
//        return appointmentRepository.save(Appointment.builder()
//                .appointmentName("appointmentName")
//                .clientId("clientId")
//                .deviceToken("deviceToken")
//                .build());
//    }
//
//    private FcmNotification createFcmNotification(Appointment appointment, LocalDateTime targetTime, NotificationStep notificationStep) {
//        return FcmNotification.builder()
//                .appointment(appointment)
//                .targetTime(targetTime)
//                .notificationStep(notificationStep)
//                .build();
//    }
//
//    private SendMessageByTokenServiceRequest createRequest(String title, String body, String deviceToken, Long notificationId) {
//        return SendMessageByTokenServiceRequest.builder()
//                .title(title)
//                .body(body)
//                .deviceToken(deviceToken)
//                .notificationId(notificationId)
//                .build();
//    }
//
//}
