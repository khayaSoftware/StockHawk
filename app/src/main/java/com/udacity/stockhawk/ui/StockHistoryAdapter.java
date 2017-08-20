package com.udacity.stockhawk.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by alex.fanning on 02/08/2017.
 */

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.HistoryViewHolder> {
    private static final String TAG = StockHistoryAdapter.class.getSimpleName();
    private ArrayList<HistoricalQuote> mAlHistory = new ArrayList<>();
    private Context context;
    private final static String DATE_FORMAT_STR = "dd-MMM-yyyy";
    private final static DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STR);
    private final DecimalFormat DOLLAR_FORMAT = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);


    public StockHistoryAdapter(ArrayList<HistoricalQuote> _alHistory,Context _c){
        mAlHistory = _alHistory;
        context = _c;
    }

    @Override
    public int getItemCount() {
        return mAlHistory.size();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.list_item_history;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem,parent,false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.bind(mAlHistory.get(position));
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView mTvDate = null;
        TextView mTvPrice = null;

        public  HistoryViewHolder(View itemView){
            super(itemView);

            mTvDate = (TextView) itemView.findViewById(R.id.tv_date_history);
            mTvPrice = (TextView) itemView.findViewById(R.id.tv_price_history);
        }


        void bind(HistoricalQuote hq){
           Calendar cal = hq.getDate();
            String dateFormatted = formatter.format(cal.getTime());
            float price = hq.getClose().floatValue();
            String priceStr = DOLLAR_FORMAT.format(price);

            mTvDate.setText(dateFormatted);
            mTvPrice.setText(priceStr);
        }


    }

}
