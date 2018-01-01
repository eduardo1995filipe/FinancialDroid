package bagarrao.financialdroid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Eduardo Bagarrao
 */
public class DateParser {

    private static Calendar cal = Calendar.getInstance();

    /**
     * Defines the default format for dates in FinancialDroid
     */
    private static final String SIMPLE_DATE_FORMAT = "dd-M-yyyy";

    /**
     * Default SimpleDateFormat used in FinancialDroid
     */
    private static final SimpleDateFormat DATE_FORMATTED = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

    /**
     * Getter for the year
     * @return Date's year
     */
    public static int getYear(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Getter for the month
     * @return Date's month
     */
    public static int getMonth(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * Getter for the day
     * @return Date's day
     */
    public static int getDay(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Getter for the hour
     * @return Date's hour
     */
    public static int getHour(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Getter for the minutes
     * @return Date's minutes
     */
    public static int getMinutes(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * Getter for the seconds
     * @return Date's seconds
     */
    public static int getSeconds(Date date) {
        cal.setTime(date);
        return cal.get(Calendar.SECOND);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String parseString(Date date){
        return DATE_FORMATTED.format(date);
    }

    /**
     *
     * @param string
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String string) throws ParseException {
        return DATE_FORMATTED.parse(string);
    }
}
