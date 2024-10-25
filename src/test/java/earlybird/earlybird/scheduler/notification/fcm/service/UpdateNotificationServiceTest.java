//package earlybird.earlybird.scheduler.notification.fcm.service;
//
//import com.google.firebase.messaging.FirebaseMessagingException;
//import earlybird.earlybird.error.exception.AlreadySentFcmNotificationException;
//import earlybird.earlybird.error.exception.FcmDeviceTokenMismatchException;
//import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
//import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
//import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
//import earlybird.earlybird.scheduler.notification.fcm.service.request.AddTaskToSchedulingTaskListServiceRequest;
//import earlybird.earlybird.scheduler.notification.fcm.service.request.UpdateFcmMessageServiceRequest;
//import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doReturn;
//
//@ActiveProfiles("test")
//@SpringBootTest
//class UpdateNotificationServiceTest {
//
//    @Autowired
//    private UpdateNotificationService service;
//
//    @Autowired
//    private FcmNotificationRepository repository;
//
//    @Autowired
//    private SchedulingTaskListService schedulingTaskListService;
//
//    @MockBean
//    private FirebaseMessagingService firebaseMessagingService;
//
//    @AfterEach
//    void tearDown() {
//        repository.deleteAllInBatch();
//    }
//
//    @DisplayName("알림 정보와 스케줄링 정보를 업데이트한다.")
//    @Test
//    void update() throws FirebaseMessagingException {
//        // given
//        String deviceToken = "deviceToken";
//        LocalDateTime savedTargetTime = LocalDateTime.now().plusDays(1);
//
//        FcmNotification notification = createFcmNotification(deviceToken, savedTargetTime);
//        FcmNotification savedNotification = repository.save(notification);
//
//        AddTaskToSchedulingTaskListServiceRequest addRequest = AddTaskToSchedulingTaskListServiceRequest.builder()
//                .body(savedNotification.getBody())
//                .deviceToken(deviceToken)
//                .title(notification.getTitle())
//                .uuid(notification.getUuid())
//                .targetTime(savedTargetTime.atZone(ZoneId.of("Asia/Seoul")).toInstant())
//                .build();
//        schedulingTaskListService.add(addRequest);
//
//        LocalDateTime updatedTargetTime = LocalDateTime.now().plusDays(2).withNano(0);
//
//        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
//                .notificationId(savedNotification.getId())
//                .deviceToken(deviceToken)
//                .targetTime(updatedTargetTime)
//                .build();
//
//        String fcmMessageId = "fcm-message-id";
//        doReturn(fcmMessageId).when(firebaseMessagingService).send(any());
//
//        // when
//        UpdateFcmMessageServiceResponse response = service.update(request);
//
//        // then
//        assertThat(response.getNotificationId()).isEqualTo(savedNotification.getId());
//        assertThat(response.getUpdatedTargetTime()).isEqualTo(updatedTargetTime);
//
//        FcmNotification updatedNotification = repository.findById(response.getNotificationId()).get();
//        assertThat(updatedNotification.getTargetTime()).isEqualTo(updatedTargetTime);
//    }
//
//    @DisplayName("현재 시간보다 과거의 시간으로 알림 시간을 수정하려고 하면 예외가 발생한다.")
//    @Test
//    void updateTargetTimeBeforeNow() {
//        // given
//        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder().targetTime(LocalDateTime.now().minusSeconds(1)).build();
//
//        // when // then
//        assertThatThrownBy(() -> service.update(request))
//                .isInstanceOf(FcmMessageTimeBeforeNowException.class);
//    }
//
//    @DisplayName("알림을 수정하려고 할 때 저장되어 있는 알림의 디바이스 토큰과 요청 디바이스 토큰이 다르면 예외가 발생한다.")
//    @Test
//    void deviceTokenMismatch() {
//        // given
//        String deviceToken = "deviceToken";
//        LocalDateTime savedTargetTime = LocalDateTime.now();
//
//        FcmNotification notification = createFcmNotification(deviceToken, savedTargetTime);
//        FcmNotification savedNotification = repository.save(notification);
//
//        String mismatchDeviceToken = "mismatchDeviceToken";
//        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
//                .deviceToken(mismatchDeviceToken)
//                .targetTime(LocalDateTime.now().plusDays(1))
//                .notificationId(savedNotification.getId())
//                .build();
//
//        // when // then
//        assertThatThrownBy(() -> service.update(request))
//                .isInstanceOf(FcmDeviceTokenMismatchException.class);
//    }
//
//    @DisplayName("이미 전송한 알림을 수정하려고 하면 예외가 발생한다.")
//    @Test
//    void alreadySentFcmNotification() {
//        // given
//        FcmNotification notification = createFcmNotification("deviceToken", LocalDateTime.now().minusDays(1));
//        notification.onSendToFcmSuccess("fcmMessageId");
//        repository.save(notification);
//
//        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
//                .notificationId(notification.getId())
//                .targetTime(LocalDateTime.now().plusDays(1))
//                .deviceToken(notification.getDeviceToken())
//                .build();
//
//        // when // then
//        assertThatThrownBy(() -> service.update(request))
//                .isInstanceOf(AlreadySentFcmNotificationException.class);
//
//    }
//
//
//    private FcmNotification createFcmNotification(String deviceToken, LocalDateTime savedTargetTime) {
//        return FcmNotification.builder()
//                .body("바디")
//                .uuid("uuid")
//                .title("제목")
//                .deviceToken(deviceToken)
//                .targetTime(savedTargetTime)
//                .build();
//    }
//
//
//}