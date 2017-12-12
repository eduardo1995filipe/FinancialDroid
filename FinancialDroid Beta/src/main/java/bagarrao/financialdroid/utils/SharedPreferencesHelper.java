package bagarrao.financialdroid.utils;

import bagarrao.financialdroid.currency.Currency;

/**
 * @author Eduardo Bagarrao
 */
public class SharedPreferencesHelper{

	/**
	 *
	 */
	public static final String ACCESS_DATE_PREF_FILE = "accessDate";

	/**
	 *
	 */
	public static final String OLD_DATE_VALUE = "oldDate";

	/**
	 *
	 */
	public static final String OLD_DATE_DEFAULT_VALUE = "nullValue"; //pratically useless

	/**
	 *
	 */
	public static final String CURRENCY_PREFERENCES_FILE = "currencyPreferences";

	/**
	 *
	 */
	public static final String CURRENCY_KEY = "defaultCurrency";

	/**
	 *
	 */
	public static final String CURRENCY_DEFAULT_VALUE = Currency.DEFAULT_CURRENCY.toString();

	/**
	 *
	 */
	public static final String CURRENCY_SPINNER_PREFERENCES_FILE = "currencySpinnerFile";

	/**
	 *
	 */
	public static final String CURRENCY_SPINNER_POSITION_KEY = "currencyPosition";

	/**
	 *
	 */
	public static final int CURRENCY_SPINNER_POSITION_DEFAULT_VALUE = 0;
}