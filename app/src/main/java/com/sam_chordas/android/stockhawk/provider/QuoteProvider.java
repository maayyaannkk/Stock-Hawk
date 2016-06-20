package com.sam_chordas.android.stockhawk.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by maayy on 11-06-2016.
 */
public class QuoteProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private QuoteDBHelper mOpenHelper;

    static final int QUOTE = 100;
    static final int QUOTE_WITH_ID = 101;
    static final int QUOTE_WITH_SYMBOL = 102;
    static final int HISTORICAL = 200;
    static final int HISTORICAL_WITH_SYMBOL = 201;

    private static final String sQuoteWithIdSelection =
            QuoteContract.Quotes.TABLE_NAME +
                    "." + QuoteContract.Quotes._ID + " = ? ";
    private static final String sHistoricalWithSymbolSelection =
            QuoteContract.Historical.TABLE_NAME +
                    "." + QuoteContract.Historical.SYMBOL + " = ? ";
    private static final String sQuoteWithSymbolSelection =
            QuoteContract.Quotes.TABLE_NAME +
                    "." + QuoteContract.Quotes.SYMBOL + " = ? ";

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QuoteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, QuoteContract.PATH_QUOTE + "/#", QUOTE_WITH_ID);
        matcher.addURI(authority, QuoteContract.PATH_QUOTE + "/*", QUOTE_WITH_SYMBOL);
        matcher.addURI(authority, QuoteContract.PATH_QUOTE, QUOTE);

        matcher.addURI(authority, QuoteContract.PATH_HISTORICAL, HISTORICAL);
        matcher.addURI(authority, QuoteContract.PATH_HISTORICAL + "/*", HISTORICAL_WITH_SYMBOL);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new QuoteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case QUOTE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.Quotes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case QUOTE_WITH_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.Quotes.TABLE_NAME,
                        projection,
                        sQuoteWithIdSelection,
                        new String[]{QuoteContract.Quotes.getQuoteIdFromUri(uri) + ""},
                        null,
                        null,
                        sortOrder
                );
                break;
            case HISTORICAL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.Historical.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case HISTORICAL_WITH_SYMBOL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuoteContract.Historical.TABLE_NAME,
                        projection,
                        sHistoricalWithSymbolSelection,
                        new String[]{QuoteContract.Historical.getSymbolFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (sUriMatcher.match(uri)) {
            case QUOTE:
                return QuoteContract.Quotes.CONTENT_TYPE;
            case QUOTE_WITH_ID:
                return QuoteContract.Quotes.CONTENT_ITEM_TYPE;
            case HISTORICAL:
                return QuoteContract.Historical.CONTENT_TYPE;
            case HISTORICAL_WITH_SYMBOL:
                return QuoteContract.Historical.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case QUOTE: {
                long _id = db.insert(QuoteContract.Quotes.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = QuoteContract.Quotes.buildQuoteUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case HISTORICAL: {
                long _id = db.insert(QuoteContract.Historical.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = QuoteContract.Historical.buildHistoricalUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        if (selection == null) selection = "1";
        int rowsDeleted;
        switch (match) {
            case QUOTE:
                rowsDeleted = db.delete(QuoteContract.Quotes.TABLE_NAME,selection,selectionArgs);
                break;
            case QUOTE_WITH_ID:
                rowsDeleted = db.delete(QuoteContract.Quotes.TABLE_NAME,sQuoteWithIdSelection,new String[]{QuoteContract.Quotes.getQuoteIdFromUri(uri)+""});
                break;
            case QUOTE_WITH_SYMBOL:
                rowsDeleted = db.delete(QuoteContract.Quotes.TABLE_NAME,sQuoteWithSymbolSelection,new String[]{QuoteContract.Quotes.getSymbolFromUri(uri)});
                break;
            case HISTORICAL:
                rowsDeleted = db.delete(QuoteContract.Historical.TABLE_NAME,selection,selectionArgs);
                break;
            case HISTORICAL_WITH_SYMBOL:
                rowsDeleted = db.delete(QuoteContract.Historical.TABLE_NAME,sHistoricalWithSymbolSelection,new String[]{QuoteContract.Historical.getSymbolFromUri(uri)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case QUOTE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(QuoteContract.Quotes.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case HISTORICAL:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(QuoteContract.Historical.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
