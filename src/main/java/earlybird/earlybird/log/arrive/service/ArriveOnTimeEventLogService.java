package earlybird.earlybird.log.arrive.service;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import earlybird.earlybird.error.exception.AppointmentNotFoundException;
import earlybird.earlybird.log.arrive.domain.ArriveOnTimeEventLog;
import earlybird.earlybird.log.arrive.domain.ArriveOnTimeEventLogRepository;
import earlybird.earlybird.log.arrive.service.request.ArriveOnTimeEventLoggingServiceRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArriveOnTimeEventLogService {

    private final ArriveOnTimeEventLogRepository arriveOnTimeEventLogRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void create(ArriveOnTimeEventLoggingServiceRequest request) {
        Long appointmentId = request.getAppointmentId();
        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(AppointmentNotFoundException::new);

        ArriveOnTimeEventLog log = new ArriveOnTimeEventLog(appointment);
        arriveOnTimeEventLogRepository.save(log);
    }
}
