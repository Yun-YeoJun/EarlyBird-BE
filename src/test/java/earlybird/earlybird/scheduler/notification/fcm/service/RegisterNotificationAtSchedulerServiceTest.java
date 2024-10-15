package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void registerFcmMessage() throws ExecutionException, InterruptedException {
        // given
        String title = "제목";
        String body = "메시지 바디";
        String deviceToken = "디바이스 토큰";
        int targetSecond = 5;
        LocalDateTime targetTime = LocalDateTime.now().plusSeconds(targetSecond);
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .title(title)
                .body(body)
                .deviceToken(deviceToken)
                .targetTime(targetTime)
                .build();

        // when
        registerNotificationAtSchedulerService.registerFcmMessage(request);

        // then
        assertThat(schedulingTaskListService.has(request.getUuid())).isTrue();

        Awaitility.await()
                .atMost(targetSecond + 1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(sendMessageToFcmService,
                            times(1)).sendMessageByToken(any(SendMessageByTokenServiceRequest.class));
                });
    }

    @DisplayName("알림 목표 시간이 현재보다 과거이면 예외가 발생한다.")
    @Test
    void targetTimeIsBeforeNow() {
        // given
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .title("제목")
                .body("바디")
                .deviceToken("디바이스토큰")
                .targetTime(LocalDateTime.now().minusSeconds(1))
                .build();

        // when // then
        assertThatThrownBy(() -> registerNotificationAtSchedulerService.registerFcmMessage(request))
                .isInstanceOf(FcmMessageTimeBeforeNowException.class);

    }

}