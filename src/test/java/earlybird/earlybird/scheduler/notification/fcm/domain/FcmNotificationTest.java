package earlybird.earlybird.scheduler.notification.fcm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FcmNotificationTest {

    @DisplayName("알림 발송 성공 상태를 업데이트한다.")
    @Test
    void onSendToFcmSuccess() {
        // given
        FcmNotification notification = FcmNotification.builder()
                .build();

        String fcmMessageId = "fcmMessageId";

        // when
        notification.onSendToFcmSuccess(fcmMessageId);

        // then
        assertThat(notification.getFcmMessageId()).isEqualTo(fcmMessageId);
        assertThat(notification.getStatus()).isEqualTo(COMPLETED);
        assertThat(notification.getSentTime()).isNotNull();
    }

    @DisplayName("알림 전송 목표 시간을 수정한다.")
    @Test
    void updateTargetTime() {
        // given
        LocalDateTime beforeTargetTime = LocalDateTime.of(2024, 10, 11, 1, 2, 3);
        FcmNotification notification = FcmNotification.builder()
                .targetTime(beforeTargetTime)
                .build();

        LocalDateTime afterTargetTime = LocalDateTime.of(2024, 12, 31, 11, 12, 13);

        // when
        notification.updateTargetTime(afterTargetTime);

        // then
        assertThat(notification.getTargetTime()).isEqualTo(afterTargetTime);
    }
}