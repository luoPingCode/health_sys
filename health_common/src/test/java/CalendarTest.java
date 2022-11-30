import com.health.utils.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author LuoPing
 * @date 2022/11/2 21:12
 */
public class CalendarTest {
    @Test
    public void calendarTest() {
        Date today = DateUtils.getToday();
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar);
        int i = calendar.get(Calendar.DATE);
        System.out.println(i);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(calendar.getTime());
    }
}
