package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.StockDetailsActivity;

/**
 * Created by alex.fanning on 03/08/2017.
 */

public class StockWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetID : appWidgetIds){
            updateAppWidget(context,appWidgetManager,appWidgetID);
        }

    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    static void updateAppWidget(Context c, AppWidgetManager awm, int appWidgetId){

        RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widget_list_view);
        Intent i = new Intent(c,StockWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view,i);

        Intent appIntent = new Intent(c, StockDetailsActivity.class);
        appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        appIntent.setData(Uri.parse(appIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getActivity(c,appWidgetId,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.symbol_widget,pendingIntent);
        awm.updateAppWidget(appWidgetId,views);


    }


}





















