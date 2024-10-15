package earlybird.earlybird.scheduler.notification.fcm.service;

import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeregisterNotificationAtSchedulerServiceTest {

    @Autowired
    private DeregisterNotificationAtSchedulerService deregisterNotificationAtSchedulerService;

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @Autowired
    private SchedulingTaskListService schedulingTaskListService;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
    }

    @DisplayName("스케줄러에 등록된 알림을 스케줄러와 DB 에서 삭제한다.")
    @Test
    void deregister() {
        // given
        LocalDateTime targetTime = LocalDateTime.now().plusDays(1);
        FcmNotification notification = createNotification(targetTime);

        FcmNotification savedNotification = fcmNotificationRepository.save(notification);

        DeregisterFcmMessageAtSchedulerServiceRequest request
                = createDeregisterRequest(savedNotification.getId(), "deviceToken");

        AddTaskToSchedulingTaskListServiceRequest addRequest = AddTaskToSchedulingTaskListServiceRequest.builder()
                .targetTime(targetTime.atZone(ZoneId.of("Asia/Seoul")).toInstant())
                .uuid("uuid")
                .title("title")
                .body("body")
                .deviceToken("deviceToken")
                .build();

        schedulingTaskListService.add(addRequest);

        // when
        deregisterNotificationAtSchedulerService.deregister(request);

        // then
        assertThat(schedulingTaskListService.has("uuid")).isFalse();
        assertThat(fcmNotificationRepository.findById(savedNotification.getId())).isEmpty();
    }

    @DisplayName("저장되어 있는 알림의 디바이스 토큰 값과 요청에 담긴 디바이스 토큰 값이 일치하지 않으면 예외가 발생한다.")
    @Test
    void deregisterWithMismatchDeviceToken() {
        // given
        FcmNotification notification = createNotification(LocalDateTime.now().plusDays(1));
        FcmNotification savedNotification = fcmNotificationRepository.save(notification);

        DeregisterFcmMessageAtSchedulerServiceRequest request
                = createDeregisterRequest(savedNotification.getId(), "mismatchedDeviceToken");

        // when // then
        assertThatThrownBy(() -> deregisterNotificationAtSchedulerService.deregister(request))
                .isInstanceOf(FcmDeviceTokenMismatchException.class);
    }

    private DeregisterFcmMessageAtSchedulerServiceRequest createDeregisterRequest(Long notificationId, String deviceToken) {
        return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
                .notificationId(notificationId)
                .deviceToken(deviceToken)
                .build();
    }

    @DisplayName("이미 전송된 알림을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void test() {
        // given
        FcmNotification notification = createNotification(LocalDateTime.now());
        notification.onSendToFcmSuccess("fcmMessageId");
        FcmNotification savedNotification = fcmNotificationRepository.save(notification);

        DeregisterFcmMessageAtSchedulerServiceRequest request
                = createDeregisterRequest(savedNotification.getId(), "deviceToken");

        // when // then
        assertThatThrownBy(() -> deregisterNotificationAtSchedulerService.deregister(request))
                .isInstanceOf(AlreadySentFcmNotificationException.class);
    }


    private static FcmNotification createNotification(LocalDateTime targetTime) {
        return FcmNotification.builder()
                .uuid("uuid")
                .title("title")
                .body("body")
                .targetTime(targetTime)
                .deviceToken("deviceToken")
                .build();
    }


}