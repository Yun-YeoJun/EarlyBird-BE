package earlybird.earlybird.scheduler.notification.fcm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationStatus {

    PENDING("전송 대기 중"),
    COMPLETED("전송 완료"),
    FAILED("전송 실패"),
    MODIFIED("변경됨"),
    POSTPONE("미뤄짐"),
    CANCELLED("취소");

    private final String text;
}
