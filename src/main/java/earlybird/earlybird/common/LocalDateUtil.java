package earlybird.earlybird.common;

import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateUtil {
  public static LocalDate getLocalDateNow() {
    return LocalDate.now(ZoneId.of("Asia/Seoul"));
  }
}
