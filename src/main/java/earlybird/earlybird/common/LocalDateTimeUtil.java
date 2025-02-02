package earlybird.earlybird.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtil {
  public static LocalDateTime getLocalDateTimeNow() {
    return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
  }

  public static LocalDateTime subtractDuration(LocalDateTime localDateTime, Duration duration) {
    if (localDateTime == null || duration == null) {
      throw new IllegalArgumentException("localDateTime and duration can't be null");
    }
    return localDateTime.minus(duration);
  }
}
