package bagarrao.financialdroid.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Eduardo
 */
public class DateForCompare {

    private Calendar cal = Calendar.getInstance();
    private Date date;
    private int year;
    private int month;
    private int day;

    /**
     * Constructor to convert a Date to a date that can be easilyCompared
     *
     * @param date Date to convert
     */
    public DateForCompare(Date date) {
        this.date = date;
        cal.setTime(date);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Getter for the Date
     *
     * @return returns the Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for the year
     *
     * @return Date's year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for the month
     *
     * @return Date's month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Getter for the day
     *
     * @return Date's day
     */
    public int getDay() {
        return day;
    }
}
