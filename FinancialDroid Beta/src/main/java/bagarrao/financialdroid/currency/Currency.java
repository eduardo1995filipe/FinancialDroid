package bagarrao.financialdroid.currency;

/**
 * Created by eduar on 23/09/2017.
 */

public enum Currency {
    EUR,USD,AUD,BRL,JPY,KRW,CNY,GBP;

    /**
     *  Default currency of the App
     */
    public static final Currency DEFAULT_CURRENCY = EUR;

    public static double getDifference(Currency currency){
        double difference = 1;
        switch (currency){
            case USD:
                difference = 0.851462387;
                break;
            case AUD:
                difference = 0.664498276;
                break;
            case BRL:
                difference = 0.266202858;
                break;
            case JPY:
                difference = 0.00753033335;
                break;
            case KRW:
                difference = 0.000739920814;
                break;
            case CNY:
                difference = 0.127435847;
                break;
            case GBP:
                difference = 1.13869897;
                break;
            default:
                break;
        }
        return difference;
    }



    public double convert(double value, Currency toConvert){
        return ((value / getDifference(this)) * getDifference(toConvert));
    }
}

