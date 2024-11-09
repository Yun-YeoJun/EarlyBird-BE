package earlybird.earlybird.log.visit.service;

import earlybird.earlybird.log.visit.domain.VisitEventLog;
import earlybird.earlybird.log.visit.domain.VisitEventLogRepository;
import earlybird.earlybird.log.visit.service.request.VisitEventLoggingServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VisitEventLogService {

    private final VisitEventLogRepository visitEventLogRepository;

    @Transactional
    public void create(VisitEventLoggingServiceRequest request) {
        visitEventLogRepository.save(new VisitEventLog(request.getClientId()));
    }
}
