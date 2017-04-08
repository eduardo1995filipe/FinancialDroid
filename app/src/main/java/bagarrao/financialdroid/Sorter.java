package bagarrao.financialdroid;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import bagarrao.financialdroid.utils.DateForCompare;
import bagarrao.financialdroid.utils.ExpenseType;


/**
 * @author Eduardo Bagarrao
 */
public class Sorter {

    /**
     * @param a List to sort by Date in a decreasing way
     * @return List sorted
     */
    public static LinkedList<Expense> sorterByDateDec(LinkedList<Expense> a) {
        Collections.sort(a, new ExternalDateComparatorDec());
        return a;
    }

    /**
     * @param a List to sort by Date in a ascending way
     * @return List sorted
     */
    public static LinkedList<Expense> sorterByDateCre(LinkedList<Expense> a) {
        Collections.sort(a, new ExternalDateComparatorCre());
        return a;
    }

    /**
     * @param a List to sort by Cost in a decreasing way
     * @return List sorted
     */
    public static LinkedList<Expense> sorterByCostDec(LinkedList<Expense> a) {
        Collections.sort(a, new ExternalCostComparatorDec());
        return a;
    }

    /**
     * @param a List to sort by Cost in a ascending way
     * @return List sorted
     */
    public static LinkedList<Expense> sorterByCostCre(LinkedList<Expense> a) {
        Collections.sort(a, new ExternalCostComparatorCre());
        return a;
    }

    /**
     * @param tipo
     * @return
     */
    public static LinkedList<Expense> getListByType(ExpenseType tipo) {
        // return GestorDespesa.getInstance().getMapa_despesas().get(tipo);
        return null;
    }

    /**
     *
     */
    public static class ExternalDateComparatorDec implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {

            DateForCompare date1 = new DateForCompare(d1.getDate());
            DateForCompare date2 = new DateForCompare(d2.getDate());

            if (date1.getYear() != date2.getYear())
                return date2.getYear() - date1.getYear();
            else if (date1.getMonth() != date2.getMonth())
                return date2.getMonth() - date1.getMonth();
            else
                return date2.getDay() - date1.getDay();
        }
    }

    /**
     *
     */
    public static class ExternalDateComparatorCre implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {

            DateForCompare date1 = new DateForCompare(d1.getDate());
            DateForCompare date2 = new DateForCompare(d2.getDate());

            if (date1.getYear() != date2.getYear())
                return date1.getYear() - date2.getYear();
            else if (date1.getMonth() != date2.getMonth())
                return date1.getMonth() - date2.getMonth();
            else
                return date1.getDay() - date2.getDay();

        }
    }

    /**
     *
     */
    public static class ExternalCostComparatorDec implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {

            return (int) ((d2.getValue() - d1.getValue()) + 0.5);
        }
    }

    /**
     *
     */
    public static class ExternalCostComparatorCre implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {

            return (int) ((d1.getValue() - d2.getValue()) + 0.5);
        }
    }
}
