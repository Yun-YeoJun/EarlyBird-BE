package earlybird.earlybird.log.arrive.domain;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.common.BaseTimeEntity;
import jakarta.persistence.*;

@Table(name = "arrive_on_time_event_log")
@Entity
public class ArriveOnTimeEventLog extends BaseTimeEntity {

  @Column(name = "arrive_on_time_event_log_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @OneToOne
  @JoinColumn(name = "appointment_id")
  private Appointment appointment;

  public ArriveOnTimeEventLog() {}

  public ArriveOnTimeEventLog(Appointment appointment) {
    this.appointment = appointment;
  }
}
