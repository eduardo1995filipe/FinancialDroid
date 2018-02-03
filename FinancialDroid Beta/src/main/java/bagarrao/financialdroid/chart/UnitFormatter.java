package bagarrao.financialdroid.chart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by eduar on 02/02/2018.
 */

public class UnitFormatter implements IValueFormatter {

    private DecimalFormat format;

    public UnitFormatter(){
        this.format = new DecimalFormat("#########0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return format.format(value) + "€";
    }
}
