package earlybird.earlybird.appointment.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import earlybird.earlybird.appointment.controller.request.CreateAppointmentRequest;
import earlybird.earlybird.appointment.service.response.CreateAppointmentServiceResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageForNewAppointmentAtSchedulerServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateAppointmentResponse {
    private final Long createdAppointmentId;

    @Builder
    private CreateAppointmentResponse(Long createdAppointmentId) {
        this.createdAppointmentId = createdAppointmentId;
    }

    public static CreateAppointmentResponse from(CreateAppointmentServiceResponse createAppointmentServiceResponse) {
        return CreateAppointmentResponse.builder()
                .createdAppointmentId(createAppointmentServiceResponse.getCreatedAppointmentId())
                .build();
    }
}
