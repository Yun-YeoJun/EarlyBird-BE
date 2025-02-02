package earlybird.earlybird.log.arrive.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ArriveOnTimeEventLoggingServiceRequest {
  private final Long appointmentId;
}
