package earlybird.earlybird.log.arrive.controller;

import earlybird.earlybird.log.arrive.controller.request.ArriveOnTimeEventLoggingRequest;
import earlybird.earlybird.log.arrive.service.ArriveOnTimeEventLogService;
import earlybird.earlybird.log.arrive.service.request.ArriveOnTimeEventLoggingServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.UpdateNotificationAtArriveOnTimeService;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateNotificationAtArriveOnTimeServiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/log/arrive-on-time-event")
@RestController
public class ArriveOnTimeEventLogController {

    private final ArriveOnTimeEventLogService arriveOnTimeEventLogService;
    private final UpdateNotificationAtArriveOnTimeService updateNotificationAtArriveOnTimeService;

    @PostMapping
    public ResponseEntity<?> arriveOnTimeEvent(@Valid @RequestBody ArriveOnTimeEventLoggingRequest request) {

        UpdateNotificationAtArriveOnTimeServiceRequest updateServiceRequest =
                UpdateNotificationAtArriveOnTimeServiceRequest.builder()
                        .appointmentId(request.getAppointmentId())
                        .clientId(request.getClientId())
                        .build();

        updateNotificationAtArriveOnTimeService.update(updateServiceRequest);

        ArriveOnTimeEventLoggingServiceRequest loggingServiceRequest =
                new ArriveOnTimeEventLoggingServiceRequest(request.getAppointmentId());
        arriveOnTimeEventLogService.create(loggingServiceRequest);

        return ResponseEntity.ok().build();
    }
}
