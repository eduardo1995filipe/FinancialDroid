package bagarrao.financialdroid.utils;

import java.util.Date;

/**
 * Created by eduar on 01/02/2017.
 */

public class AccessDate {

    private Date date;
    private long id;

    public AccessDate(Date date) {
        this.date = date;
        this.id = -1;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}


