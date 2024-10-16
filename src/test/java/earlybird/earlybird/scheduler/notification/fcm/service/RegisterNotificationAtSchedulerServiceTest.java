package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class RegisterNotificationAtSchedulerServiceTest {

    @MockBean
    private SendMessageToFcmService sendMessageToFcmService;

    @Autowired
    private RegisterNotificationAtSchedulerService registerNotificationAtSchedulerService;

    @Autowired
    private SchedulingTaskListService schedulingTaskListService;

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
    }

    @DisplayName("FCM 메시지를 스케줄러에 등록하면 등록된 시간에 FCM 메시지 전송이 실행된다.")
    @Test
    void schedulerExecute() throws ExecutionException, InterruptedException {
        // given
        int targetSecond = 5;
        LocalDateTime targetTime = LocalDateTime.now().plusSeconds(targetSecond);
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .clientId("clientId")
                .deviceToken("deviceToken")
                .appointmentName("appointmentName")
                .preparationTime(LocalDateTime.now().minusHours(1))
                .movingTime(LocalDateTime.now().minusHours(1))
                .appointmentTime(LocalDateTime.now().plusSeconds(targetSecond))
                .build();

        // when
        registerNotificationAtSchedulerService.registerFcmMessage(request);

        // then
        Awaitility.await()
                .atMost(targetSecond + 1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(sendMessageToFcmService,
                            times(1)).sendMessageByToken(any(SendMessageByTokenServiceRequest.class));
                });
    }

    @DisplayName("요청한 준비 시간, 이동 시간, 약속 시간에 따라 최대 7개의 알림이 스케줄러에 등록된다.")
    @Test
    void registerFcmMessage() throws ExecutionException, InterruptedException {
        // given
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .clientId("clientId")
                .deviceToken("deviceToken")
                .appointmentName("appointmentName")
                .preparationTime(LocalDateTime.now().plusHours(2))
                .movingTime(LocalDateTime.now().plusHours(3))
                .appointmentTime(LocalDateTime.now().plusHours(4))
                .build();

        // when
        RegisterFcmMessageAtSchedulerServiceResponse response = registerNotificationAtSchedulerService.registerFcmMessage(request);

        // then
        assertThat(response.getNotifications()).hasSize(7);
    }

    @DisplayName("알림 목표 시간이 현재보다 과거이면 알림이 등록되지 않는다.")
    @Test
    void targetTimeIsBeforeNow() {
        // given
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .clientId("clientId")
                .deviceToken("deviceToken")
                .appointmentName("appointmentName")
                .preparationTime(LocalDateTime.now().minusHours(1))
                .movingTime(LocalDateTime.now().minusHours(1))
                .appointmentTime(LocalDateTime.now().minusHours(1))
                .build();

        // when
        RegisterFcmMessageAtSchedulerServiceResponse response = registerNotificationAtSchedulerService.registerFcmMessage(request);

        // then
        assertThat(response.getNotifications()).hasSize(0);

    }

}