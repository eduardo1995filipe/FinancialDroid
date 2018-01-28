package bagarrao.financialdroid.expense;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import bagarrao.financialdroid.utils.DateParser;

/**
 *
 * @author Eduardo Bagarrao 2017-2018
 *
 * Class that shows an expense. It's optimised
 * for Firebase backend management.
 * That's now the standard expense object.
 *
 * @since 0.1.8
 *
 */
@IgnoreExtraProperties
public class Expenditure{

    @Exclude
    private long id;

    public double value;

    private String description;

    @Exclude
    private Date date;

    private ExpenseType type;

    private long time;

    @Nullable
    private String uid;

    public Expenditure(){
    }

    public Expenditure(double value, ExpenseType type, String description, Date date){
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.time = date.getTime();
    }

    public Expenditure(double value, ExpenseType type, String description, Date date,String uid){
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.uid = uid;
        this.time = date.getTime();
    }

    public Expenditure(double value, ExpenseType type, String description, long time,String uid){
        this.value = value;
        this.type = type;
        this.description = description;
        this.time = time;
        this.date = new Date((time * 1000));
        this.uid = uid;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public ExpenseType getType() {
        return type;
    }
    public void setType(ExpenseType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    public Date getDate() {
        return date != null ? date : new Date(time);
    }
    @Exclude
    public void setDate(Date date) {
        this.date = date;
        this.time = date.getTime();
    }

    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
        this.date = new Date(time);
    }

    @Nullable
    public String getUid() {
        return this.uid;
    }
    public void setUid(@Nullable String uid) {
        this.uid = uid;
    }

    @Exclude
    public long getId() {
        return id;
    }
    @Exclude
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        Expenditure other = (Expenditure) obj;
        if (other.date.equals(date) &&
                other.description.equals(description) &&
                other.type.equals(type) &&
                ((int)(other.value + 0.5)) == ((int)(value + 0.5)) &&
                (other.getUid().equals(uid) || uid.equals("")))
            return true;
        return false;
    }

    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("value",value);
        map.put("type",type);
        map.put("description",description);
        map.put("date",time);
        map.put("uid",uid);
        return map;
    }

    @Exclude
    public boolean isOld(){
        Date currentDate = new Date();
        return ((DateParser.getMonth(getDate()) < DateParser.getMonth(currentDate) &&
                DateParser.getYear(getDate()) == DateParser.getYear(currentDate)) ||
                (DateParser.getYear(getDate()) < DateParser.getYear(currentDate)));
    }

    @Override
    public String toString() {
        return value + ";" + type.toString() + ";" + description + ";" + (date != null ? DateParser.parseString(date) : DateParser.parseString(new Date(time)));
    }
}
