package bagarrao.financialdroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import bagarrao.financialdroid.utils.ExpenseType;


public class Expense {

    private double value;
    private ExpenseType type;
    private String description;
    private Date date;
    private long id;

    /**
     * @param value       Expense price
     * @param type        type of the Expense
     * @param description of the Expense
     * @param date        When the Expense was made
     */
    public Expense(double value, ExpenseType type, String description, Date date) {
        this.value = value;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    /**
     * Getter of the Expense value
     *
     * @return value of the expense
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter of the Expense type
     *
     * @return Expense type
     */
    public ExpenseType getType() {
        return type;
    }

    /**
     * Getter of the Expense description
     *
     * @return Expense description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter of the Expense date
     *
     * @return date of the Expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        Expense other = (Expense) obj;
        if (other.date.equals(this.date) && other.description.equals(this.description) && other.type.equals(this.type)
                && other.value == this.value)
            return true;
        return false;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        String stringData = df.format(date);
        return value + ";" + type.toString() + ";" + description + ";" + stringData;
    }

}
