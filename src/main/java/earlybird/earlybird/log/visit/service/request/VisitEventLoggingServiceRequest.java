package earlybird.earlybird.log.visit.service.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VisitEventLoggingServiceRequest {

    private final String clientId;
}
