package bagarrao.financialdroid.Expense;

import android.util.Log;

import java.util.List;

import bagarrao.financialdroid.utils.Sorter;

/**
 * @author Eduardo Bagarrao
 */
public enum ExpenseOrder {

    DATE_DESCENDING, DATE_ASCENDING, PRICE_DESCENDING, PRICE_ASCENDING;

    /**
     * Sort the list in a way that depends of the current enum that is
     *
     * @param listToSort List to be sorted
     * @return Sorted list
     */
    public List<Expense> sortByOrder(List<Expense> listToSort) {
        switch (this) {
            case DATE_ASCENDING:
                listToSort = Sorter.sorterByDateCre(listToSort);
                break;
            case DATE_DESCENDING:
                listToSort = Sorter.sorterByDateDec(listToSort);
                break;
            case PRICE_ASCENDING:
                listToSort = Sorter.sorterByCostCre(listToSort);
                break;
            case PRICE_DESCENDING:
                listToSort = Sorter.sorterByCostDec(listToSort);
                break;
            default:
                Log.e("ExpenseOrder", "Unknown error while sorting the list. List returned unsorted");
                break;
        }
        return listToSort;
    }
}
