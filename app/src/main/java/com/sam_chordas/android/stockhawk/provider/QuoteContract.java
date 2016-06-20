package com.sam_chordas.android.stockhawk.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by maayy on 11-06-2016.
 */
public class QuoteContract {
    public static final String CONTENT_AUTHORITY = "com.sam_chordas.android.stockhawk.data.QuoteProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_QUOTE = "quotes";
    public static final String PATH_HISTORICAL = "hist";

    public static final class Quotes implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUOTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUOTE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUOTE;

        public static final String TABLE_NAME = "quote";

        public static final String SYMBOL = "symbol";

        public static final String PERCENT_CHANGE = "percent_change";

        public static final String CHANGE = "change";

        public static final String BIDPRICE = "bid_price";

        public static final String CREATED = "created";

        public static final String ISUP = "is_up";

        public static final String ISCURRENT = "is_current";

        public static Uri buildQuoteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildQuoteSymbol(String symbol) {
            return CONTENT_URI.buildUpon().appendPath(symbol).build();
        }

        public static String getSymbolFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static long getQuoteIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class Historical implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISTORICAL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICAL;

        public static final String TABLE_NAME = "historical";

        public static final String SYMBOL = "symbol";

        public static final String DATE = "date";

        public static final String OPEN = "open";

        public static final String CLOSE = "close";

        public static final String HIGH = "high";

        public static final String LOW = "low";

        public static final String VOLUME = "volume";

        public static Uri buildHistoricalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri withSymbol(String symbol){
            return CONTENT_URI.buildUpon().appendPath(symbol).build();
        }
        public static String getSymbolFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
