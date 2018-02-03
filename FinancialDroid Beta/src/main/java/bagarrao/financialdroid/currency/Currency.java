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

    public double convertToEuro(double value) {
        double euroValue = 0.0;
        switch (this) {
            case USD:
                euroValue = 0.802407222;
                break;
            case AUD:
                euroValue = 0.636228686;
                break;
            case BRL:
                euroValue = 0.249315948;
                break;
            case JPY:
                euroValue = 0.00728585757;
                break;
            case KRW:
                euroValue = 0.000736609829;
                break;
            case CNY:
                euroValue = 0.127301906;
                break;
            case GBP:
                euroValue = 1.1332317;
                break;
            case EUR:
                euroValue = 1;
                break;
            default:
                break;
        }
        return euroValue * value;
    }

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

