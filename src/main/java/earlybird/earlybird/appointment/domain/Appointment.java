package earlybird.earlybird.appointment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @Column(nullable = false)
    private String appointmentName;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String deviceToken;

    @JsonIgnoreProperties({"appointment"})
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_id")
    private List<FcmNotification> fcmNotifications = new ArrayList<>();

    public void addFcmNotification(FcmNotification fcmNotification) {
        fcmNotifications.add(fcmNotification);
    }

    public void removeFcmNotification(FcmNotification fcmNotification) {
        fcmNotifications.remove(fcmNotification);
    }

    @Builder
    private Appointment(String appointmentName, String clientId, String deviceToken) {
        this.appointmentName = appointmentName;
        this.clientId = clientId;
        this.deviceToken = deviceToken;
    }
}
