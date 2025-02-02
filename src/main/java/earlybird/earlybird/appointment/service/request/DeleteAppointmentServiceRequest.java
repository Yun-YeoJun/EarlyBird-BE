package earlybird.earlybird.appointment.service.request;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.*;

import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteAppointmentServiceRequest {

  private final String clientId;
  private final Long appointmentId;

  @Builder
  public DeleteAppointmentServiceRequest(String clientId, Long appointmentId) {
    this.clientId = clientId;
    this.appointmentId = appointmentId;
  }

  public DeregisterFcmMessageAtSchedulerServiceRequest
      toDeregisterFcmMessageAtSchedulerServiceRequest() {
    return DeregisterFcmMessageAtSchedulerServiceRequest.builder()
        .clientId(clientId)
        .appointmentId(appointmentId)
        .targetNotificationStatus(CANCELLED)
        .build();
  }
}
