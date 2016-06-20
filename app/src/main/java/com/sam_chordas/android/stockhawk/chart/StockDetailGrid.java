package com.sam_chordas.android.stockhawk.chart;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.provider.QuoteContract;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by maayy on 20-06-2016.
 */
public class StockDetailGrid {

    TextView symbol,date,high,low,open,close,volume;
    Cursor cursor;

    public StockDetailGrid(GridLayout gl, Cursor cursor){
        symbol = (TextView) gl.findViewById(R.id.textViewSymbol);
        date = (TextView) gl.findViewById(R.id.textViewGridDate);
        high = (TextView) gl.findViewById(R.id.textViewGridHigh);
        low = (TextView) gl.findViewById(R.id.textViewGridLow);
        open = (TextView) gl.findViewById(R.id.textViewGridOpen);
        close = (TextView) gl.findViewById(R.id.textViewGridClose);
        volume = (TextView) gl.findViewById(R.id.textViewGridVolume);

        this.cursor = cursor;
    }
    public void init(Context context){
        cursor.moveToFirst();

        symbol.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.SYMBOL))+" Details");
        date.setText(Utils.formatDate(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.DATE))));
        high.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.HIGH)));
        low.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.LOW)));
        open.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.OPEN)));
        close.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.CLOSE)));
        volume.setText(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.VOLUME)));

    }
}
