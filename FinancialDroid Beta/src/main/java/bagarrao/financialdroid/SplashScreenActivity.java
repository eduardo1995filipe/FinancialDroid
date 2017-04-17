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
//            Toast.makeText(this, "Migration will run", Toast.LENGTH_SHORT).show();
            new Migrator(this).run();
//            Toast.makeText(this, "Migration completed successfully", Toast.LENGTH_SHORT).show();
        }
        this.intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * @return
     */
    public boolean needsMigration() {
        try {
            //inicio
            this.sharedPreferences = getSharedPreferences(ACCESS_DATE, Context.MODE_PRIVATE);
            this.sharedPreferencesEditor = sharedPreferences.edit();
            Date currentDate = new Date();

            if (!sharedPreferences.contains(OLD_DATE)) {
//                Toast.makeText(this, "Nao existe data anterior", Toast.LENGTH_SHORT).show();
                putNewDate(currentDate);
                return false;
            } else {
                Date lastDate = DateForCompare.DATE_FORMATTED.parse(sharedPreferences.getString(OLD_DATE, NULL_DATE));

                //datas para comparar
                DateForCompare currentForCompare = new DateForCompare(currentDate);
                DateForCompare lastForCompare = new DateForCompare(lastDate);

//                Toast.makeText(this, "mes atual --> " + currentForCompare.getMonth()  + "\n" +
//                        "mes anterior --> " + lastForCompare.getMonth(), Toast.LENGTH_SHORT).show();

                if (currentForCompare.getMonth() > lastForCompare.getMonth()) {
                    putNewDate(currentDate);
//                    Toast.makeText(this, "Vou devolver true porque mudou o mês", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    putNewDate(currentDate);
                }
            }
        } catch (ParseException e) {
//            Toast.makeText(this, "Estou na excecao", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        Toast.makeText(this, "Vou dar false como ultima opcao", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * @param date
     */
    public void putNewDate(Date date) {
        if (sharedPreferences.contains(OLD_DATE))
            sharedPreferencesEditor.remove(OLD_DATE);
        sharedPreferencesEditor.putString(OLD_DATE, DateForCompare.DATE_FORMATTED.format(date));
        sharedPreferencesEditor.commit();
    }
}
