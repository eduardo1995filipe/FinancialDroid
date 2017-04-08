package bagarrao.financialdroid.utils;

public enum ExpenseType {

    FEEDING, TRANSPORTS, SCHOOL, CLOTHES, OTHERS;

    /**
     * Converts a String to an Enum(ExpensiveTypeObject)
     *
     * @param expenseType String to convert to a ExpensiveType object
     * @return ExpensiveType object converted from the String
     */
    public static ExpenseType strToExpenseType(String expenseType) {
        for (ExpenseType td : ExpenseType.values()) {
            if (td.toString().equals(expenseType)) {
                return td;
            }
        }
        return null;
    }

}
