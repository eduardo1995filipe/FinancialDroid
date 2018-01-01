package bagarrao.financialdroid.expense;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bagarrao.financialdroid.utils.DateParser;

@IgnoreExtraProperties
public class Expenditure {

    public float value;
    private ExpenseType type;
    private String description;
    private Date date;

    public Expenditure(){
    }

    public Expenditure(float value, ExpenseType type, String description, Date date){
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("value",value);
        map.put("type",type);
        map.put("description",description);
        map.put("date",date);
        return map;
    }
}
