package bagarrao.financialdroid.utils;

import java.util.Date;

/**
 * @author Eduardo Bagarrao
 */
public class AccessDate {

    private Date date;
    private long id;

    /**
     * Contructor to initiate a new AccessDate Object.
     * Whenever you start the app, a new AccessDate is instanciated.
     *
     * @param date Date when um accessed FinancialDroid
     */
    public AccessDate(Date date) {
        this.date = date;
        this.id = -1;
    }

    /**
     * Getter of the date of your AccessDate object
     * @return Date of the AccessDate object
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter of the ID of your AccessDate object
     * @return the ID of the AccessDate object
     */
    public long getId() {
        return id;
    }

    /**
     * Seter for the AccessDate ID
     * @param id id that you want to set for your AccessDate Object
     */
    public void setId(long id) {
        this.id = id;
    }
}


