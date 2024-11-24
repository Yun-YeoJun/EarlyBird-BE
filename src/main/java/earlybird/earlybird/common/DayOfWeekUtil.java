package earlybird.earlybird.common;


import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DayOfWeekUtil {

    /**
     * @param dayOfWeekBooleanList 월화수목금토일
     */
    public static List<DayOfWeek> convertBooleanListToDayOfWeekList(List<Boolean> dayOfWeekBooleanList) {
        if (dayOfWeekBooleanList == null || dayOfWeekBooleanList.size() != 7)
            throw new IllegalArgumentException("dayOfWeekBooleanList must contain exactly 7 days");

        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (dayOfWeekBooleanList.get(i))
                dayOfWeekList.add(DayOfWeek.of(i + 1));
        }

        return dayOfWeekList;
    }
}
