package earlybird.earlybird.appointment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import earlybird.earlybird.common.BaseTimeEntity;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SQLDelete(sql = "UPDATE appointment SET is_deleted = true WHERE appointment_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Appointment extends BaseTimeEntity {

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    private List<FcmNotification> fcmNotifications = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appointment")
    @SQLRestriction("is_deleted = false")
    private List<RepeatingDay> repeatingDays = new ArrayList<>();

    private LocalTime appointmentTime;
    private Duration preparationDuration;
    private Duration movingDuration;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    private Appointment(
            String appointmentName,
            String clientId,
            String deviceToken,
            LocalTime appointmentTime,
            Duration preparationDuration,
            Duration movingDuration,
            List<DayOfWeek> repeatingDayOfWeeks) {
        this.appointmentName = appointmentName;
        this.clientId = clientId;
        this.deviceToken = deviceToken;
        this.appointmentTime = appointmentTime;
        this.preparationDuration = preparationDuration;
        this.movingDuration = movingDuration;
        setRepeatingDays(repeatingDayOfWeeks);
    }

    public List<RepeatingDay> getRepeatingDays() {
        return repeatingDays.stream().filter(repeatingDay -> !repeatingDay.isDeleted()).toList();
    }

    public void addFcmNotification(FcmNotification fcmNotification) {
        this.fcmNotifications.add(fcmNotification);
        fcmNotification.setAppointment(this);
    }

    public void removeFcmNotification(FcmNotification fcmNotification) {
        fcmNotifications.remove(fcmNotification);
    }

    public void addRepeatingDay(RepeatingDay repeatingDay) {
        if (repeatingDays.stream().noneMatch(r -> r.getId().equals(repeatingDay.getId()))) {
            this.repeatingDays.add(repeatingDay);
        }
    }

    public void setRepeatingDays(List<DayOfWeek> dayOfWeeks) {
        setRepeatingDaysEmpty();
        dayOfWeeks.forEach(dayOfWeek -> this.repeatingDays.add(new RepeatingDay(dayOfWeek, this)));
    }

    public void setRepeatingDaysEmpty() {
        this.repeatingDays.forEach(RepeatingDay::setDeleted);
    }

    public void changeAppointmentName(String newName) {
        this.appointmentName = newName;
    }

    public void changeDeviceToken(String newToken) {
        this.deviceToken = newToken;
    }

    public void changePreparationDuration(Duration newDuration) {
        this.preparationDuration = newDuration;
    }

    public void changeMovingDuration(Duration newDuration) {
        this.movingDuration = newDuration;
    }

    public void changeAppointmentTime(LocalTime newTime) {
        this.appointmentTime = newTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted() {
        this.isDeleted = true;
        setRepeatingDaysEmpty();
    }
}
