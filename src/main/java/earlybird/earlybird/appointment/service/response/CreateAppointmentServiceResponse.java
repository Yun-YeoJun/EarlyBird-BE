package earlybird.earlybird.appointment.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateAppointmentServiceResponse {

  private final Long createdAppointmentId;

  @Builder
  private CreateAppointmentServiceResponse(Long createdAppointmentId) {
    this.createdAppointmentId = createdAppointmentId;
  }
}
