package earlybird.earlybird.log.visit.domain;

import earlybird.earlybird.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VisitEventLog extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String clientId;

    public VisitEventLog() {}

    public VisitEventLog(String clientId) {
        this.clientId = clientId;
    }
}
