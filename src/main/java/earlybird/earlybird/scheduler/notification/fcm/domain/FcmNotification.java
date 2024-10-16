package earlybird.earlybird.scheduler.notification.fcm.domain;

import earlybird.earlybird.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String body;

    @Column(nullable = false)
    private String deviceToken;

    @Column(nullable = false)
    private LocalDateTime targetTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FcmNotificationStatus status = PENDING;

    private LocalDateTime sentTime;

    /**
     * FCM 에 메시지 등록 성공 후 리턴 받게 되는 message id 값
     */
    private String fcmMessageId;

    @Builder
    private FcmNotification(String uuid, String title, String body, String deviceToken, LocalDateTime targetTime) {
        this.uuid = uuid;
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
        this.targetTime = targetTime;
    }

    public void onSendToFcmSuccess(String fcmMessageId) {
        this.fcmMessageId = fcmMessageId;
        this.sentTime = LocalDateTime.now();
        changeStatusToCompleted();
    }

    public void updateTargetTime(LocalDateTime targetTime) {
        this.targetTime = targetTime;
    }

    private void changeStatusToCompleted() {
        this.status = COMPLETED;
    }
}
