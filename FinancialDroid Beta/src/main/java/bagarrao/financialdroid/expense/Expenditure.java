package bagarrao.financialdroid.expense;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bagarrao.financialdroid.firebase.FirebaseManager;
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
 * @see Expense
 *
 */
@IgnoreExtraProperties
public class Expenditure {

    /**
     * {@link FirebaseManager} singleton call.
     */
    private FirebaseManager manager = FirebaseManager.getInstance();

    /**
     * {@link Expenditure} value.
     */
    public float value;

    /**
     * {@link Expenditure} type.
     *
     * @see ExpenseType
     */
    private ExpenseType type;

    /**
     * {@link Expenditure} description.
     */
    private String description;

    /**
     * {@link Expenditure} date.
     */
    private Date date;

    /**
     * {@link Expenditure} uid from the current
     * {@link com.google.firebase.auth.FirebaseUser}
     * (or inserted in {@link #Expenditure(float, ExpenseType, String, Date, String)}
     * constructor {@link com.google.firebase.auth.FirebaseAuth}.
     *
     * @see FirebaseManager
     */
    private String uid;

    /**
     * {@link #Expenditure()} Default constructor required for calls to
     * {@link com.google.firebase.database.DataSnapshot}.getValue({@link Expenditure}.class)
     */
    public Expenditure(){
    }

    /**
     * {@link #Expenditure(float, ExpenseType, String, Date)}
     * constructor for the expenditures inserted by the own user.
     *
     * @param value float
     * @param type {@link ExpenseType}
     * @param description {@link String}
     * @param date {@link Date}
     */
    public Expenditure(float value, ExpenseType type, String description, Date date){
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.uid = manager.getUid();
    }

    /**
     * {@link #Expenditure(float, ExpenseType, String, Date, String)}
     * constructor used for expenditures that are read by the user.
     *
     * @param value float
     * @param type {@link ExpenseType}
     * @param description {@link String}
     * @param date {@link Date}
     * @param uid {@link String}
     */
    public Expenditure(float value, ExpenseType type, String description, Date date,String uid){
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
        this.uid = uid;
    }

    /**
     * Getter for {@link #value}.
     * @return float
     */
    public float getValue() {
        return value;
    }

    /**
     * Setter for {@link #value}.
     * @param value float
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Getter for {@link #type}.
     * @return ExpenseType
     */
    public ExpenseType getType() {
        return type;
    }

    /**
     * Setter for {@link #type}.
     * @param type {@link ExpenseType}
     */
    public void setType(ExpenseType type) {
        this.type = type;
    }

    /**
     * Getter for {@link #description}.
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for {@link #description}.
     * @param description {@link String}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for {@link #date}.
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for {@link #date}.
     * @param date {@link Date}
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for {@link #uid}.
     * @return String
     */
    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        Expenditure other = (Expenditure) obj;
        if (other.date.equals(date) &&
                other.description.equals(description) &&
                other.type.equals(type) &&
                ((int)(other.value + 0.5)) == ((int)(value + 0.5)) &&
                other.getUid().equals(uid))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return value + ";" + type.toString() + ";" + description + ";" + DateParser.parseString(date);
    }

    /**
     * Export own object to a {@link HashMap<String,Object>}
     * with the {@link Expenditure} attributes, {@link #value},
     * {@link #type}, {@link #description}, {@link #date},
     * {@link #uid} and his values.
     *
     * @return {@link HashMap<String,Object>} that contains {@link Expenditure} attributes.
     *
     */
    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("value",value);
        map.put("type",type);
        map.put("description",description);
        map.put("date",date);
        map.put("uid",uid);
        return map;
    }
}
