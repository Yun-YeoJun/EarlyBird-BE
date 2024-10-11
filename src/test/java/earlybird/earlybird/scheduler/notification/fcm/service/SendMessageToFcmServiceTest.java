package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class SendMessageToFcmServiceTest {

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @Autowired
    private SendMessageToFcmService sendMessageToFcmService;

    @MockBean
    private FirebaseMessagingService firebaseMessagingService;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
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
        FcmNotification fcmNotification = createFcmNotification(title, body, uuid, deviceToken, targetTime);
        fcmNotificationRepository.save(fcmNotification);

        String fcmMessageId = "fcm-message-id";
        doReturn(fcmMessageId).when(firebaseMessagingService).send(request);

        // when
        SendMessageByTokenServiceResponse response = sendMessageToFcmService.sendMessageByToken(request).get();

        // then
        assertThat(fcmNotificationRepository.findByUuid(uuid)).isPresent();
        assertThat(fcmNotificationRepository.findByUuid(uuid).get()).extracting(
                FcmNotification::getUuid, FcmNotification::getTitle, FcmNotification::getBody,
                FcmNotification::getDeviceToken, FcmNotification::getTargetTime, FcmNotification::getStatus, FcmNotification::getFcmMessageId)
                .contains(
                        uuid, title, body, deviceToken, targetTime, COMPLETED, fcmMessageId
                );
    }

    private FcmNotification createFcmNotification(String title, String body, String uuid, String deviceToken, LocalDateTime targetTime) {
        return FcmNotification.builder()
                .title(title)
                .body(body)
                .uuid(uuid)
                .deviceToken(deviceToken)
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