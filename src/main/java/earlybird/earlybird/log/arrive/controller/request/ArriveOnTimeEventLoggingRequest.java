package earlybird.earlybird.log.arrive.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArriveOnTimeEventLoggingRequest {

    @NotNull private Long appointmentId;

    @NotBlank private String clientId;
}
