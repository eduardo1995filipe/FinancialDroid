//package bagarrao.financialdroid.currency;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//import bagarrao.financialdroid.database.DataSource;
//import bagarrao.financialdroid.expense.Expenditure;
//import bagarrao.financialdroid.database.DatabaseManager;
//import bagarrao.financialdroid.utils.SharedPreferencesHelper;
//
///**
// * @author Eduardo Bagarrao
// */
//public class CurrencyConverter {
//
//
//    private DatabaseManager manager = DatabaseManager.getInstance();
//    private static final CurrencyConverter INSTANCE = new CurrencyConverter();
//    private boolean isInitialized;
//
//    /**
//     *
//     */
//    private SharedPreferences sharedPref;
//
//    /**
//     *
//     */
//    private SharedPreferences.Editor sharedPrefEditor;
//
//    private Context context;
//
//    /**
//     *
//     */
//    private CurrencyConverter(){
//        this.isInitialized = false;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public static CurrencyConverter getInstance() {
//        return ((INSTANCE != null) ? INSTANCE : new CurrencyConverter());
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//        if(context != null)
//            this.isInitialized = true;
//    }
//
//    /**
//     *
//     * @param context
//     */
//    public void init(Context context){
//        setContext(context);
//        this.sharedPref = context.getSharedPreferences(SharedPreferencesHelper.CURRENCY_PREFERENCES_FILE, context.MODE_PRIVATE);
//    }
//
//    /**
//     *
//     * @return
//     */
//    public Currency getCurrency(){
//        if(isInitialized)
//            return Currency.valueOf(sharedPref.getString(SharedPreferencesHelper.CURRENCY_KEY, SharedPreferencesHelper.CURRENCY_DEFAULT_VALUE));
//        else
//            throw new NullPointerException("Context needs to be initialized before you use this Singleton!");
//    }
//
//
//
//    /**
//     *
//     * @param currency
//     */
//    public void setCurrency(Currency currency){
//        //TODO: a mudar
//        if(isInitialized){
//            Currency lastCurrency = getCurrency();
//            this.sharedPrefEditor = sharedPref.edit();
//            sharedPrefEditor.putString(SharedPreferencesHelper.CURRENCY_KEY, currency.toString());
//            sharedPrefEditor.apply();
////
////            if(manager.isLocal())
////                updateLocalExpenditures(currency, lastCurrency);
////            else
////                manager.notifyCurrencyChange(lastCurrency);
//        }else
//            throw new NullPointerException("Context needs to be initialized before you use this Singleton");
//    }
//
//    /**
//     *
//     * @param currency
//     * @param lastCurrency
//     */
//    public void updateLocalExpenditures(Currency currency, Currency lastCurrency) {
//        List<Expenditure> expenses, listToDelete;
//        listToDelete = expenses = manager.getAllExpenditures();
//        for(Expenditure e : listToDelete){
//            manager.removeExpenditure(e);
//        }
//        for(Expenditure e : expenses){
//            e.setValue(lastCurrency.convert(e.getValue(),currency));
//            manager.insertExpenditure(e);
//        }
//    }
//
//    /**
//     *
//     * @param value
//     * @param places
//     * @return
//     */
//    public static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
//}
