package bagarrao.financialdroid;

import java.util.List;

/**
 * Created by eduar on 11/04/2017.
 */

public enum ExpenseOrder {

    DATE_DESCENDING, DATE_ASCENDING, PRICE_DESCENDING, PRICE_ASCENDING;

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
                System.out.println("ERRO DESCONHECIDO DE ORDENACAO DA LISTA");
                break;
        }
        return listToSort;
    }

}
