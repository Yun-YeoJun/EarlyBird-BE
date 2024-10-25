package earlybird.earlybird.scheduler.notification.fcm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationStep {

    ONE_HOUR_BEFORE_PREPARATION_TIME(1L, " 준비 1시간 전!", "오늘의 준비사항을 확인해봐요 \uD83D\uDE0A"),
    FIVE_MINUTES_BEFORE_PREPARATION_TIME(2L, "5분 후에 준비 시작해야 해요!", "허겁지겁 준비하면 후회해요! \uD83E\uDEE2"),
    PREPARATION_TIME(3L, "지금 준비 시작 안하면 늦어요 ❗\uFE0F❗\uFE0F❗\uFE0F", "같이 5초 세고, 시작해봐요!"),
    TEN_MINUTES_BEFORE_MOVING_TIME(4L, "10분 후에 이동해야 안 늦어요!", "교통정보를 미리 확인해보세요  \uD83D\uDEA5"),
    MOVING_TIME(5L, "지금 출발해야 안 늦어요 ❗\uFE0F❗\uFE0F❗\uFE0F", "준비사항 다 체크하셨나요?"),
    FIVE_MINUTES_BEFORE_APPOINTMENT_TIME(6L, "약속장소에 도착하셨나요?!", "도착하셨으면 확인버튼을 눌러주세요! \uD83E\uDD29"),
    APPOINTMENT_TIME(7L, "1분 안에 확인버튼을 눌러주세요!!", "안 누르면 지각처리돼요!!! \uD83D\uDEAB\uD83D\uDEAB\uD83D\uDEAB");

    private final Long stepNumber;
    private final String title;
    private final String body;
}
