package earlybird.earlybird.appointment.controller;

import earlybird.earlybird.appointment.controller.request.CreateAppointmentRequest;
import earlybird.earlybird.appointment.controller.request.UpdateAppointmentRequest;
import earlybird.earlybird.appointment.controller.response.CreateAppointmentResponse;
import earlybird.earlybird.appointment.service.CreateAppointmentService;
import earlybird.earlybird.appointment.service.DeleteAppointmentService;
import earlybird.earlybird.appointment.service.UpdateAppointmentService;
import earlybird.earlybird.appointment.service.request.CreateAppointmentServiceRequest;
import earlybird.earlybird.appointment.service.request.DeleteAppointmentServiceRequest;
import earlybird.earlybird.appointment.service.request.UpdateAppointmentServiceRequest;
import earlybird.earlybird.appointment.service.response.CreateAppointmentServiceResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@RestController
public class AppointmentController {

    private final CreateAppointmentService createAppointmentService;
    private final UpdateAppointmentService updateAppointmentService;
    private final DeleteAppointmentService deleteAppointmentService;

    @PostMapping
    public ResponseEntity<CreateAppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {

        CreateAppointmentServiceRequest serviceRequest =
                request.toCreateAppointmentServiceRequest();
        CreateAppointmentServiceResponse serviceResponse =
                createAppointmentService.create(serviceRequest);

        return ResponseEntity.ok(CreateAppointmentResponse.from(serviceResponse));
    }

    @PatchMapping
    public ResponseEntity<Void> updateAppointment(
            @Valid @RequestBody UpdateAppointmentRequest request) {

        UpdateAppointmentServiceRequest serviceRequest =
                request.toUpdateAppointmentServiceRequest();
        updateAppointmentService.update(serviceRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAppointment(
            @RequestHeader("appointmentId") Long appointmentId,
            @RequestHeader("clientId") String clientId) {

        DeleteAppointmentServiceRequest serviceRequest =
                new DeleteAppointmentServiceRequest(clientId, appointmentId);
        deleteAppointmentService.delete(serviceRequest);

        return ResponseEntity.noContent().build();
    }
}
