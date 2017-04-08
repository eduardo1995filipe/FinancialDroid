package bagarrao.financialdroid.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    public static final String DAY_IN_WEEK = "EEE";
    public static final String MONTH_IN_YEAR = "MMM";
    public static final String DAY_IN_MONTH = "dd";
    public static final String HOUR_IN_DAY = "H";
    public static final String MINUTE_IN_HOUR = "mm";
    public static final String SECOND_IN_MINUTE = "ss";
    public static final String TIME_ZONE = "z";
    public static final String YEAR = "yyyy";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String SIMPLE_DATE_FORMAT = DAY_IN_WEEK + SPACE +
            MONTH_IN_YEAR + SPACE + DAY_IN_MONTH + SPACE + HOUR_IN_DAY +
            COLON + MINUTE_IN_HOUR + COLON + SECOND_IN_MINUTE + SPACE +
            TIME_ZONE + YEAR;
    public static final DateFormat FORMAT = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

    /**
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date strToDefaultDateFormat(String str) throws ParseException {
        return FORMAT.parse(str);
    }

    /**
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date strToAccessDateFormat(String str) throws ParseException {
        return FORMAT.parse(str);
    }
}
