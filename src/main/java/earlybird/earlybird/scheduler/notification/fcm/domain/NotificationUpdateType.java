package earlybird.earlybird.scheduler.notification.fcm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationUpdateType {

    POSTPONE("약속 미루기"),
    MODIFY("일정 수정하기");

    private final String type;
}
