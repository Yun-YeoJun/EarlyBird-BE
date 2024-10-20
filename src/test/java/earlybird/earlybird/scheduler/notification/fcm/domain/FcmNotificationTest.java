package earlybird.earlybird.scheduler.notification.fcm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;

class FcmNotificationTest {

    @DisplayName("알림 발송 성공 상태를 업데이트한다.")
    @Test
    void onSendToFcmSuccess() {
        // given
        FcmNotification notification = FcmNotification.builder()
                .build();

        // when
        notification.onSendToFcmSuccess();

        // then
        assertThat(notification.getStatus()).isEqualTo(COMPLETED);
        assertThat(notification.getSentTime()).isNotNull();
    }

    @DisplayName("알림 상태를 수정한다.")
    @Test
    void updateStatusTo() {
        // given
        FcmNotification notification = FcmNotification.builder().build();

        // when
        notification.updateStatusTo(COMPLETED);

        // then
        assertThat(notification.getStatus()).isEqualTo(COMPLETED);
    }
}