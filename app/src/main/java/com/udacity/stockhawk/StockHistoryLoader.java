package com.udacity.stockhawk;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.udacity.stockhawk.data.Contract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by alex.fanning on 02/08/2017.
 */

public class StockHistoryLoader extends AsyncTaskLoader<ArrayList<HistoricalQuote>> {
    public static final int HISTORY_LOADER_ID = 20;
    private static final String TAG = StockHistoryLoader.class.getSimpleName();
    private Context c;
    private String mSymbol;

    private static final String COMMA_DELIMMITER = ",";


    public StockHistoryLoader(Context _c, String _mSymbol){
        super(_c);
        c = _c;
        mSymbol = _mSymbol;
    }


    @Override
    public ArrayList<HistoricalQuote> loadInBackground() {
        Cursor cursor = null;
        String fullHistString;
        ArrayList<HistoricalQuote> alHistory;

        Uri uri = Contract.Quote.URI;
        uri = uri.buildUpon().appendPath(mSymbol).build();
        cursor = c.getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();

        fullHistString = cursor.getString(Contract.Quote.POSITION_HISTORY);
        return getQuotesFromString(fullHistString);


    }

    private ArrayList<HistoricalQuote> getQuotesFromString(String fullHistQuote){
        ArrayList<HistoricalQuote> alHistory = new ArrayList<>();
        String[] historyLines =  fullHistQuote.split(c.getString(R.string.new_line_char));
        for (String line : historyLines){
            String[] historyParts = line.split(COMMA_DELIMMITER);
            String dateInMillis = historyParts[0];
            long ms = Long.parseLong(dateInMillis);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(ms);
            String price =  historyParts[1].trim();
            HistoricalQuote hq = new HistoricalQuote();
            hq.setDate(cal);
            hq.setClose(new BigDecimal(price));
            alHistory.add(hq);
        }
        return alHistory;
    }




}
