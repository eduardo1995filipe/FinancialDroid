package bagarrao.financialdroid.expense;

import android.util.Log;

/**
 * @author Eduardo Bagarrao
 */
public enum ExpenseType {

    FEEDING, TRANSPORTS, SCHOOL, CLOTHING, OTHERS;

    public static ExpenseType getTypeByIndex(int index) {
        ExpenseType type = null;
        switch (index) {
            case 0:
                break;
            case 1:
                type = ExpenseType.FEEDING;
                break;
            case 2:
                type = ExpenseType.TRANSPORTS;
                break;
            case 3:
                type = ExpenseType.CLOTHING;
                break;
            case 4:
                type = ExpenseType.SCHOOL;
                break;
            case 5:
                type = ExpenseType.OTHERS;
                break;
            default:
                Log.d("Filter", "getTypeByIndex: unexpected error, index value is out of bounds");
                break;
        }
        return type;

    }
}
