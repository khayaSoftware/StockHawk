package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHistoryLoader;
import com.udacity.stockhawk.data.Contract;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

public class StockDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<HistoricalQuote>> {
    private static final String TAG = StockDetailsActivity.class.getSimpleName();
    private final DecimalFormat DOLLAR_FORMAT = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);

    private String mSymbol;
    private float mCurrentPrice;
    private RecyclerView mRvHistory;
    private TextView mTvSymbolHeader;
    private TextView mTvPriceHeader;
    private StockHistoryAdapter mHisAdapter;
    private TextView mTvErrorRv;

    private static final String CURRENT_PRICE_STR = "Current Price: ";
    private static final String HISTORY_STR = " History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        setUpActionBar();
        findViews();

        mRvHistory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvHistory.setHasFixedSize(true);
        getAndSetHeaderValues();

        getSupportLoaderManager().initLoader(StockHistoryLoader.HISTORY_LOADER_ID,null,this);
        setUpLoader();

    }

    private void setUpLoader(){
        LoaderManager lm = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<HistoricalQuote>> hisLoader = lm.getLoader(StockHistoryLoader.HISTORY_LOADER_ID);
        if (hisLoader == null){
            lm.initLoader(StockHistoryLoader.HISTORY_LOADER_ID,null,this);
        }else{
            lm.restartLoader(StockHistoryLoader.HISTORY_LOADER_ID,null,this).forceLoad();
        }
    }


    private void setUpActionBar(){
        ActionBar ab = this.getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findViews(){
        mTvSymbolHeader = (TextView) findViewById(R.id.tv_symbol_header);
        mTvPriceHeader = (TextView) findViewById(R.id.tv_price_header);
        mRvHistory = (RecyclerView)findViewById(R.id.rv_history);
        mTvErrorRv = (TextView) findViewById(R.id.rv_error);
    }

    private void getAndSetHeaderValues(){
        Intent i = getIntent();
        if (i.hasExtra(getString(R.string.intent_symbol_key)) && i.hasExtra(getString(R.string.intent_price_key))){
            mSymbol = i.getStringExtra(getString(R.string.intent_symbol_key));
            mCurrentPrice = i.getFloatExtra(getString(R.string.intent_price_key),0);

            mTvSymbolHeader.setText(mSymbol);
            mTvPriceHeader.setText(CURRENT_PRICE_STR + DOLLAR_FORMAT.format(mCurrentPrice));
            setTitle(mSymbol + HISTORY_STR );

        }
    }

    @Override
    public android.support.v4.content.Loader<ArrayList<HistoricalQuote>> onCreateLoader(int id, Bundle args) {

            StockHistoryLoader sl = new StockHistoryLoader(this,mSymbol);
            return sl;

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<HistoricalQuote>> loader, ArrayList<HistoricalQuote> alHq) {
        displayHistory(alHq);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<HistoricalQuote>> loader) {
        //Do nothing
    }

    private void displayHistory(ArrayList<HistoricalQuote> alHq){
        if (alHq == null){
            mTvErrorRv.setVisibility(View.VISIBLE);
            mRvHistory.setVisibility(View.GONE);
        }else{
            mTvErrorRv.setVisibility(View.GONE);
            mRvHistory.setVisibility(View.VISIBLE);
            mHisAdapter = new StockHistoryAdapter(alHq,this);
            mRvHistory.setAdapter(mHisAdapter);
        }

    }

}

