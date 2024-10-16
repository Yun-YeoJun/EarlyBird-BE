package earlybird.earlybird.scheduler.notification.fcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import earlybird.earlybird.appointment.domain.Appointment;
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
    @Column(name = "fcm_notification_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String body;

    @Column(nullable = false)
    private LocalDateTime targetTime;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FcmNotificationStatus status = PENDING;

    @JsonIgnore
    private LocalDateTime sentTime;

    /**
     * FCM 에 메시지 등록 성공 후 리턴 받게 되는 message id 값
     */
    @JsonIgnore
    private String fcmMessageId;

    @Builder
    private FcmNotification(String uuid, Appointment appointment, String title, String body, LocalDateTime targetTime) {
        this.uuid = uuid;
        this.appointment = appointment;
        this.title = title;
        this.body = body;
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
