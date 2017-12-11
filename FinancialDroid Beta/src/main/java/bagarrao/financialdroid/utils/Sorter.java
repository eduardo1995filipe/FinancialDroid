package bagarrao.financialdroid.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bagarrao.financialdroid.expense.Expense;

/**
 * @author Eduardo Bagarrao
 */
public class Sorter {

    /**
     * @param a List to sort by Date in a decreasing way
     * @return List sorted
     */
    public static List<Expense> sorterByDateDec(List<Expense> a) {
        Collections.sort(a, new ExternalDateComparatorDec());
        return a;
    }

    /**
     * @param a List to sort by Date in a ascending way
     * @return List sorted
     */
    public static List<Expense> sorterByDateCre(List<Expense> a) {
        Collections.sort(a, new ExternalDateComparatorCre());
        return a;
    }

    /**
     * @param a List to sort by Cost in a decreasing way
     * @return List sorted
     */
    public static List<Expense> sorterByCostDec(List<Expense> a) {
        Collections.sort(a, new ExternalCostComparatorDec());
        return a;
    }

    /**
     * @param a List to sort by Cost in a ascending way
     * @return List sorted
     */
    public static List<Expense> sorterByCostCre(List<Expense> a) {
        Collections.sort(a, new ExternalCostComparatorCre());
        return a;
    }

    /**
     *Comparator that sorts Expense Objects by Date in descending order
     */
    public static class ExternalDateComparatorDec implements Comparator<Expense> {

        @Override
        public int compare(Expense e1, Expense e2) {
            return(DateParser.getYear(e1.getDate()) != DateParser.getYear(e2.getDate())) ?
                    DateParser.getYear(e2.getDate()) - DateParser.getYear(e1.getDate()) :
                    (DateParser.getMonth(e1.getDate()) != DateParser.getMonth(e2.getDate())) ?
                            DateParser.getMonth(e2.getDate()) - DateParser.getMonth(e1.getDate()) :
                            DateParser.getDay(e2.getDate()) - DateParser.getDay(e1.getDate());
        }
    }


    /**
     *Comparator that sorts Expense Objects by Date in ascending order
     */
    public static class ExternalDateComparatorCre implements Comparator<Expense> {

        @Override
        public int compare(Expense e1, Expense e2) {

            return(DateParser.getYear(e1.getDate()) != DateParser.getYear(e2.getDate())) ?
                    DateParser.getYear(e1.getDate()) - DateParser.getYear(e2.getDate()) :
                    (DateParser.getMonth(e1.getDate()) != DateParser.getMonth(e2.getDate())) ?
                            DateParser.getMonth(e1.getDate()) - DateParser.getMonth(e2.getDate()) :
                            DateParser.getDay(e1.getDate()) - DateParser.getDay(e2.getDate());
        }
    }

    /**
     *Comparator that sorts Expense Objects by cost in descendingg order
     */
    public static class ExternalCostComparatorDec implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {
            return (int) ((d2.getValue() - d1.getValue()) + 0.5);
        }
    }

    /**
     *Comparator that sorts Expense Objects by cost in ascending order
     */
    public static class ExternalCostComparatorCre implements Comparator<Expense> {

        @Override
        public int compare(Expense d1, Expense d2) {
            return (int) ((d1.getValue() - d2.getValue()) + 0.5);
        }
    }
}
