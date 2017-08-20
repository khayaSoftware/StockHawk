package com.udacity.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by alex.fanning on 04/08/2017.
 */

public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetRemoteViewsFactory(this.getApplicationContext());
    }
}

class StockWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private static final String TAG = StockWidgetRemoteViewsFactory.class.getSimpleName();
    private static final DecimalFormat DOLLAR_FORMAT = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);


    Context mContext;
    Cursor mCursor;

    public StockWidgetRemoteViewsFactory(Context appContext){
        mContext = appContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                Contract.Quote.URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        try{
            mCursor.close();
        }catch(Exception e){
            Log.e(TAG, "onDestroy: Error");
        }

    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();

    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        String symbol = mCursor.getString(Contract.Quote.POSITION_SYMBOL);
        float price = mCursor.getFloat(Contract.Quote.POSITION_PRICE);
        String priceStr = DOLLAR_FORMAT.format(price);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_stock_view);
        views.setTextViewText(R.id.symbol_widget,symbol);
        views.setTextViewText(R.id.price_widget,priceStr);

        Bundle extras = new Bundle();
        extras.putString(mContext.getString(R.string.intent_symbol_key),symbol);
        extras.putString(mContext.getString(R.string.intent_price_key),priceStr);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.symbol_widget,fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}