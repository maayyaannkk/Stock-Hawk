package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.provider.QuoteContract;

/**
 * Created by maayy on 20-06-2016.
 */
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(QuoteContract.Quotes.CONTENT_URI,
                        new String[]{QuoteContract.Quotes._ID, QuoteContract.Quotes.SYMBOL, QuoteContract.Quotes.BIDPRICE,
                                QuoteContract.Quotes.PERCENT_CHANGE, QuoteContract.Quotes.CHANGE, QuoteContract.Quotes.ISUP},
                        QuoteContract.Quotes.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_item);

                views.setTextViewText(R.id.widget_stock_symbol, data.getString(data.getColumnIndex(QuoteContract.Quotes.SYMBOL)));
                views.setTextViewText(R.id.widget_bid_price, data.getString(data.getColumnIndex(QuoteContract.Quotes.BIDPRICE)));
                if (data.getInt(data.getColumnIndex("is_up")) == 1){
                    views.setInt(R.id.widget_change,"setBackgroundResource",R.drawable.percent_change_pill_green);
                } else{
                    views.setInt(R.id.widget_change,"setBackgroundResource",R.drawable.percent_change_pill_red);
                }
                views.setTextViewText(R.id.widget_change, data.getString(data.getColumnIndex(QuoteContract.Quotes.CHANGE)));

                final Intent fillInIntent = new Intent();
                Uri histUri = QuoteContract.Historical.withSymbol(data.getString(data.getColumnIndex(QuoteContract.Quotes.SYMBOL)));
                fillInIntent.setData(histUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndex(QuoteContract.Quotes._ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
