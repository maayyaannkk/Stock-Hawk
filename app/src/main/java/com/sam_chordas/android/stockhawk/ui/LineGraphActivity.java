package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalColumns;

public class LineGraphActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    Uri mUri;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        mUri = getIntent().getData();
        tv = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (mUri != null) {
            return new CursorLoader(this, mUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Toast.makeText(this, "Uri is "+mUri.toString(), Toast.LENGTH_LONG).show();
            tv.setText(cursor.getString(cursor.getColumnIndex(HistoricalColumns.DATE)));
        } else {
            Log.e("load finished", "data null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
