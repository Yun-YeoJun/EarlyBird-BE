package earlybird.earlybird.scheduler.notification.fcm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.common.BaseTimeEntity;
import earlybird.earlybird.common.LocalDateTimeUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static earlybird.earlybird.scheduler.notification.fcm.domain.NotificationStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_notification_id")
    private Long id;

    @Setter
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStep notificationStep;

    @Column(nullable = false)
    private LocalDateTime targetTime;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = PENDING;

    @JsonIgnore
    private LocalDateTime sentTime;

    @Builder
    private FcmNotification(Appointment appointment, NotificationStep notificationStep, LocalDateTime targetTime) {
        this.appointment = appointment;
        this.notificationStep = notificationStep;
        this.targetTime = targetTime;
    }

    public void onSendToFcmSuccess() {
        this.sentTime = LocalDateTimeUtil.getLocalDateTimeNow();
        updateStatusTo(COMPLETED);
    }

    public void onSendToFcmFailure() {
        this.sentTime = null;
        updateStatusTo(FAILED);
    }

    public void updateStatusTo(NotificationStatus status) {
        this.status = status;
    }
}
