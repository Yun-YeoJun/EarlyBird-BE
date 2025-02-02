package earlybird.earlybird.appointment.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.service.request.DeleteAppointmentServiceRequest;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteAppointmentService {

  private final FindAppointmentService findAppointmentService;
  private final DeregisterNotificationService deregisterNotificationService;

  @Transactional
  public void delete(DeleteAppointmentServiceRequest request) {
    Appointment appointment =
        findAppointmentService.findBy(request.getAppointmentId(), request.getClientId());
    deregisterNotificationService.deregister(
        request.toDeregisterFcmMessageAtSchedulerServiceRequest());
    appointment.setRepeatingDaysEmpty();
    appointment.setDeleted();
  }
}
