package earlybird.earlybird.log.visit.controller.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VisitEventLoggingRequest {

  private String clientId;
}
