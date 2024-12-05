package earlybird.earlybird.appointment.controller.response;

import earlybird.earlybird.appointment.service.response.CreateAppointmentServiceResponse;
import lombok.*;

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
