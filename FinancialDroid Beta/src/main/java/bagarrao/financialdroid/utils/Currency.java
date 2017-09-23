package bagarrao.financialdroid.utils;

/**
 * Created by eduar on 23/09/2017.
 */

public enum Currency {
    EUR,USD,AUD,BRL,JPY,KRW,CNY,GBP;

    public static final Currency DEFAULT_CURRENCY = EUR;

    public static double getDifference(Currency currency){
        double difference = 1;
        switch (currency){
            case USD:
                difference = 1.1951;
                break;
            case AUD:
                difference = 1.51555694;
                break;
            case BRL:
                difference = 3.73480421;
                break;
            case JPY:
                difference = 133.844775;
                break;
            case KRW:
                difference = 1354.98866;
                break;
            case CNY:
                difference = 7.87986681;
                break;
            case GBP:
                difference = 0.885521636;
                break;
            default:
                break;
        }
        return difference;
    }



    public static double convert(double value,Currency currencyFrom, Currency currencyTo){
        return (value / getDifference(currencyFrom)) * getDifference(currencyTo);
    }
}

