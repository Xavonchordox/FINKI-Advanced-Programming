package Lab4.P2;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * LocalTime API tests
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        System.out.println(localTimeOfHourToMinute());
        System.out.println(localTimeOfHourToNanoSec());
        System.out.println(localTimeParse());
        System.out.println(localTimeWith());
        System.out.println(localTimePlus());
        System.out.println(localTimeMinus());
        System.out.println(localTimeMinusDuration());
        System.out.println(localDateIsBefore());
        System.out.println(localTimeTruncatedTo());
    }

    static LocalTime localTimeOfHourToMinute() {
        /**
         * Create a {@link LocalTime} of 23:07 by using {@link LocalTime#of}
         */
        LocalTime time = LocalTime.of(23,7);
        return time;
    }

    static LocalTime localTimeOfHourToNanoSec() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 by using {@link LocalTime#of}
         */
        LocalTime time = LocalTime.of(23, 7, 3, 100000000);
        return time;
    }

    static LocalTime localTimeParse() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 from String by using {@link LocalTime#parse}
         */
        String str = "23:07:03.1";
        LocalTime time = LocalTime.parse(str);
        return time;
    }

    static LocalTime localTimeWith() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with hour 21
         * by using {@link LocalTime#withHour} or {@link LocalTime#with}
         */
        LocalTime time = lt.withHour(21);
        return time;
    }

    static LocalTime localTimePlus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 30 minutes later
         * by using {@link LocalTime#plusMinutes} or {@link LocalTime#plus}
         */
        LocalTime time = lt.plusMinutes(30);
        return time;
    }

    static LocalTime localTimeMinus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 3 hours before
         * by using {@link LocalTime#minusHours} or {@link LocalTime#minus}
         */
        LocalTime time = lt.minusHours(3);

        return time;
    }


    static LocalTime localTimeMinusDuration() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Define a {@link Duration} of 3 hours 30 minutes and 20.2 seconds
         * Create a {@link LocalTime} subtracting the duration from {@link lt} by using {@link LocalTime#minus}
         */
        Duration duration = Duration.ofSeconds(3*60*60+30*60+20);
        Duration duration2 = Duration.ofNanos(200000000);

        LocalTime time = lt.minus(duration).minus(duration2);
        return time;
    }

    static boolean localDateIsBefore() {
        LocalTime lt = DateAndTimes.LT_23073050;
        LocalTime lt2 = DateAndTimes.LT_12100000;
        /**
         * Check whether {@link lt2} is before {@link lt} or not
         * by using {@link LocalTime#isAfter} or {@link LocalTime#isBefore}
         */
        boolean time = lt2.isBefore(lt);

        return time;
    }

    static LocalTime localTimeTruncatedTo() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} truncated to minutes by using {@link LocalTime#truncatedTo}
         */
        LocalTime time = lt.truncatedTo(ChronoUnit.MINUTES);

        return time;
    }

    static class DateAndTimes {
        public static final LocalTime LT_23073050 = LocalTime.of(23, 7, 30, 500000000);
        public static final LocalTime LT_12100000 = LocalTime.of(12, 10);
    }

}
