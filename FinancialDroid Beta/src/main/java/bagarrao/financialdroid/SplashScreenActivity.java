package bagarrao.financialdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.ParseException;
import java.util.Date;

import bagarrao.financialdroid.backup.Backup;
import bagarrao.financialdroid.migration.Migrator;
import bagarrao.financialdroid.utils.DateForCompare;

/**
 * @author Eduardo Bagarrao
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final String ACCESS_DATE = "accessDate";
    private static final String NULL_DATE = "nullValue";
    private static final String OLD_DATE = "oldDate";

    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Backup(this).go();
        if (needsMigration()) {
			new Migrator(this).run();
		}
        this.intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
	 * checks if its's necessary or not to migrate the expenses
     * @return boolean value that indicates if it's necessary to migrate the expenses
     */
    public boolean needsMigration() {
        try {
            this.sharedPreferences = getSharedPreferences(ACCESS_DATE, Context.MODE_PRIVATE);
            this.sharedPreferencesEditor = sharedPreferences.edit();
            Date currentDate = new Date();
            if (!sharedPreferences.contains(OLD_DATE)) {
				putNewDate(currentDate);
                return false;
            } else {
                Date lastDate = DateForCompare.DATE_FORMATTED.parse(sharedPreferences.getString(OLD_DATE, NULL_DATE));
                DateForCompare currentForCompare = new DateForCompare(currentDate);
                DateForCompare lastForCompare = new DateForCompare(lastDate);
                if ((lastForCompare.getMonth() < currentForCompare.getMonth() && lastForCompare.getYear() == currentForCompare.getYear()) ||
                        (lastForCompare.getYear() < currentForCompare.getYear())) {
                    putNewDate(currentDate);
					return true;
                } else {
                    putNewDate(currentDate);
                }
            }
        } catch (ParseException e) {
			e.printStackTrace();
        }
        return false;
    }

    /**
	 * saves the last date that the app was opened on SharedPreferences
     * @param date Date that will be saved on SharedPreferences
     */
    public void putNewDate(Date date) {
        if (sharedPreferences.contains(OLD_DATE))
            sharedPreferencesEditor.remove(OLD_DATE);
        sharedPreferencesEditor.putString(OLD_DATE, DateForCompare.DATE_FORMATTED.format(date));
        sharedPreferencesEditor.commit();
    }
}
