package earlybird.earlybird.common;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtil {
    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
