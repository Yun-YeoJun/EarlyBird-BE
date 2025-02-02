package earlybird.earlybird.scheduler.notification.service.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import earlybird.earlybird.error.exception.NotificationNotFoundException;
import earlybird.earlybird.scheduler.notification.domain.CreateTestFcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.domain.NotificationStatus;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateNotificationStatusServiceTest {

  @Mock private FcmNotificationRepository notificationRepository;

  @InjectMocks private UpdateNotificationStatusService service;

  @DisplayName("푸시 알림 전송 성공 시 푸시 알림 정보를 성공으로 업데이트한다.")
  @Test
  void updateToSuccess() {
    // given
    FcmNotification notification = CreateTestFcmNotification.create();

    Long notificationId = 1L;

    when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

    // when
    service.update(notificationId, true);

    // then
    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.COMPLETED);
  }

  @DisplayName("푸시 알림 전송 실패 시 푸시 알림 정보를 실패로 업데이트한다.")
  @Test
  void updateToFailure() {
    // given
    FcmNotification notification = CreateTestFcmNotification.create();
    Long notificationId = 1L;
    when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

    // when
    service.update(notificationId, false);

    // then
    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
  }

  @DisplayName("업데이트 대상 알림이 존재하지 않으면 예외가 발생한다.")
  @Test
  void throwExceptionWhenNotificationNotFound() {
    // given
    Long notificationId = 1L;
    when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

    // when // then
    assertThatThrownBy(() -> service.update(notificationId, true))
        .isInstanceOf(NotificationNotFoundException.class);
    assertThatThrownBy(() -> service.update(notificationId, false))
        .isInstanceOf(NotificationNotFoundException.class);
  }
}
