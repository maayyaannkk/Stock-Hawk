package com.sam_chordas.android.stockhawk.chart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.provider.QuoteContract;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by maayy on 15-06-2016.
 */
public class StockHistoryCard {

    private final LineChartView mChart;
    private final TextView startDate, endDate, title, legend;
    private Cursor cursor;
    float max = 0, min = 0;

    private String[] mLabels;
    private float[] mValuesHigh = {}, mValuesLow = {};

    public StockHistoryCard(CardView card, Context context, Cursor cursor) {
        mChart = (LineChartView) card.findViewById(R.id.chart2);
        startDate = (TextView) card.findViewById(R.id.textViewDateStart);
        endDate = (TextView) card.findViewById(R.id.textViewDateEnd);
        title = (TextView) card.findViewById(R.id.chart_legend);
        legend = (TextView) card.findViewById(R.id.textViewLegend);
        try {
            legend.setText(Html.fromHtml("<font color=\"#53c1bd\"><b>___</b></font> High <br/> <font color=\"#3f7178\"><b>___</b></font> Low"));
            this.cursor = cursor;
            if (this.cursor != null) {

                cursor.moveToFirst();

                startDate.setText(Utils.formatDate(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.DATE))));

                title.setText(context.getString(R.string.stock_history_legend,cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.SYMBOL))));

                mLabels = new String[cursor.getCount()];
                mValuesHigh = new float[cursor.getCount()];
                mValuesLow = new float[cursor.getCount()];
                for (int i = 0; i < cursor.getCount(); i++) {
                    mLabels[i] = "";
                    mValuesHigh[i] = Float.parseFloat(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.HIGH)));
                    mValuesLow[i] = Float.parseFloat(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.LOW)));
                    if (i == 0) {
                        max = mValuesHigh[i];
                        min = mValuesLow[i];
                    } else {
                        if (mValuesHigh[i] > max) max = mValuesHigh[i];
                        if (mValuesLow[i] < min) min = mValuesLow[i];
                    }
                    cursor.moveToNext();
                }
                cursor.moveToPrevious();
                endDate.setText(Utils.formatDate(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.DATE))));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        if (cursor != null) {
            mChart.reset();
            LineSet dataset = new LineSet(mLabels, mValuesHigh);
            dataset.setColor(Color.parseColor("#53c1bd"))
                    .setThickness(3);
            //.setFill(Color.parseColor("#3d6c73"))
            //.setGradientFill(new int[]{Color.parseColor("#364d5a"), Color.parseColor("#3f7178")}, null);
            mChart.addData(dataset);

            dataset = new LineSet(mLabels, mValuesLow);
            dataset.setColor(Color.parseColor("#53c1bd"))
                    .setFill(Color.parseColor("#3d6c73"))
                    .setThickness(1)
                    .setGradientFill(new int[]{Color.parseColor("#364d5a"), Color.parseColor("#3f7178")}, null);
            mChart.addData(dataset);

            mChart.setBorderSpacing(1)
                    .setXLabels(AxisController.LabelPosition.NONE)
                    .setYLabels(AxisController.LabelPosition.OUTSIDE)
                    .setXAxis(false)
                    .setYAxis(true)
                    .setAxisBorderValues(Math.round(min) - 1, Math.round(max) + 1)
                    .setStep(Math.round(max - min) / 10 > 0 ? Math.round(max - min) / 10 : 1)
                    .setBorderSpacing(Tools.fromDpToPx(0));

            mChart.show();

        }
    }
}
