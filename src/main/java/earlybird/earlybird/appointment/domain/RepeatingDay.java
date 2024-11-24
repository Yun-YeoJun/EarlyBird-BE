package earlybird.earlybird.appointment.domain;

import earlybird.earlybird.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;
import org.springframework.lang.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@SQLDelete(sql = "UPDATE repeating_day SET is_deleted = true WHERE repeating_day_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RepeatingDay extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repeating_day_id", nullable = false)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "appointemnt_id", nullable = false)
    private Appointment appointment;

    private Boolean isDeleted = false;

    public void setDeleted() {
        this.isDeleted = true;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    protected RepeatingDay(@NonNull DayOfWeek dayOfWeek, @NonNull Appointment appointment) {
        this.dayOfWeek = dayOfWeek;
        this.appointment = appointment;
    }
}
