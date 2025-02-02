package earlybird.earlybird.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantUtil {
    public static boolean checkTimeBeforeNow(Instant time) {
        return (time.isBefore(Instant.now()));
    }

    public static ZonedDateTime getZonedDateTimeFromInstant(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }
}
