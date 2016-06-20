package com.sam_chordas.android.stockhawk.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maayy on 11-06-2016.
 */
public class QuoteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "quotes.db";

    public QuoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_QUOTE_TABLE = "CREATE TABLE " + QuoteContract.Quotes.TABLE_NAME + " (" +
                QuoteContract.Quotes._ID + " INTEGER PRIMARY KEY ," +
                QuoteContract.Quotes.SYMBOL + " TEXT NOT NULL, " +
                QuoteContract.Quotes.PERCENT_CHANGE + " TEXT NOT NULL, " +
                QuoteContract.Quotes.CHANGE + " TEXT NOT NULL, " +
                QuoteContract.Quotes.BIDPRICE + " TEXT NOT NULL, " +
                QuoteContract.Quotes.CREATED + " TEXT , " +
                QuoteContract.Quotes.ISUP + " INTEGER NOT NULL, " +
                QuoteContract.Quotes.ISCURRENT + " INTEGER NOT NULL, " +
                " UNIQUE (" + QuoteContract.Quotes.SYMBOL + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_HISTORICAL_TABLE = "CREATE TABLE " + QuoteContract.Historical.TABLE_NAME + " (" +
                QuoteContract.Historical._ID + " INTEGER PRIMARY KEY ," +
                QuoteContract.Historical.SYMBOL + " TEXT NOT NULL, " +
                QuoteContract.Historical.DATE + " TEXT NOT NULL, " +
                QuoteContract.Historical.OPEN + " TEXT NOT NULL, " +
                QuoteContract.Historical.CLOSE + " TEXT NOT NULL, " +
                QuoteContract.Historical.HIGH + " TEXT NOT NULL, " +
                QuoteContract.Historical.LOW + " TEXT NOT NULL, " +
                QuoteContract.Historical.VOLUME + " TEXT NOT NULL, " +
                " UNIQUE ("+ QuoteContract.Historical.SYMBOL+","+ QuoteContract.Historical.DATE+") ON CONFLICT REPLACE,"+
                " FOREIGN KEY (" + QuoteContract.Historical.SYMBOL + ") REFERENCES " +
                QuoteContract.Quotes.TABLE_NAME + " (" + QuoteContract.Quotes.SYMBOL + ") " +
                " );";

        db.execSQL(SQL_CREATE_QUOTE_TABLE);
        db.execSQL(SQL_CREATE_HISTORICAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuoteContract.Quotes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuoteContract.Historical.TABLE_NAME);
        onCreate(db);
    }
}
