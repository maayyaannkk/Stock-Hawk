package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.chart.StockDetailGrid;
import com.sam_chordas.android.stockhawk.chart.StockHistoryCard;
import com.sam_chordas.android.stockhawk.provider.QuoteContract;
import com.sam_chordas.android.stockhawk.rest.Utils;

public class LineGraphActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    final private String PROGRESS_STATE = "progress";
    final private String SEEK_START_DATE = "seek_start";
    final private String SEEK_END_DATE = "seek_end";
    private static final int CURSOR_LOADER_ID = 0;
    Uri mUri;
    TextView seekStartDate, seekEndDate;
    SeekBar seekBar;
    boolean initial = true;
    int offset = 0, limit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        mUri = getIntent().getData();
        seekStartDate = (TextView) findViewById(R.id.textViewSeekStart);
        seekEndDate = (TextView) findViewById(R.id.textViewSeekEnd);
        seekBar = (SeekBar) findViewById(R.id.seekBarDate);

        if (savedInstanceState != null) {
            initial = false;
            seekBar.setProgress(savedInstanceState.getInt(PROGRESS_STATE));
            seekStartDate.setText(savedInstanceState.getString(SEEK_START_DATE));
            seekEndDate.setText(savedInstanceState.getString(SEEK_END_DATE));
        }

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PROGRESS_STATE, seekBar.getProgress());
        outState.putString(SEEK_START_DATE, seekStartDate.getText().toString());
        outState.putString(SEEK_END_DATE, seekEndDate.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (mUri != null && initial) {
            return new CursorLoader(this, mUri, null, null, null, QuoteContract.Historical.DATE + " ASC");
        } else if (mUri != null && limit != 0 && offset != 0) {
            return new CursorLoader(this, mUri, null, null, null, QuoteContract.Historical.DATE + " ASC limit " + limit + " offset " + offset);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

        if (initial) {
            cursor.moveToFirst();
            seekStartDate.setText(Utils.formatDate(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.DATE))));
            cursor.moveToLast();
            seekEndDate.setText(Utils.formatDate(cursor.getString(cursor.getColumnIndex(QuoteContract.Historical.DATE))));

            seekBar.setProgress(0);
            seekBar.setMax(cursor.getCount() - 5);
        }
        (new StockHistoryCard((CardView) findViewById(R.id.card2),LineGraphActivity.this, cursor)).init();
        (new StockDetailGrid((GridLayout) findViewById(R.id.detail_grid_layout), cursor)).init(LineGraphActivity.this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                initial = false;
                offset = seekBar.getProgress() == 0 ? 1 : seekBar.getProgress();
                limit = 2000;

                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, LineGraphActivity.this);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        (new StockHistoryCard((CardView) findViewById(R.id.card2),LineGraphActivity.this, null)).init();
    }
}
