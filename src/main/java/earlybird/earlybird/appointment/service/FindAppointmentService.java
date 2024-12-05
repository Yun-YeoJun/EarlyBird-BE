package earlybird.earlybird.appointment.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.error.exception.DeletedAppointmentException;
import earlybird.earlybird.scheduler.notification.service.deregister.request.DeregisterFcmMessageAtSchedulerServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindAppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment findBy(Long appointmentId, String clientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(AppointmentNotFoundException::new);

        if (!appointment.getClientId().equals(clientId))
            throw new AppointmentNotFoundException();

        if (appointment.isDeleted())
            throw new DeletedAppointmentException();

        return appointment;
    }

    public Appointment findBy(DeregisterFcmMessageAtSchedulerServiceRequest request) {
        return findBy(request.getAppointmentId(), request.getClientId());
    }
}
