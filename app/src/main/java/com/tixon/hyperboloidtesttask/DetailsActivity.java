package com.tixon.hyperboloidtesttask;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private static final String PRICES = "prices";
    private static final String DESCRIPTIONS = "descriptions";
    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String LONG_TEXT = "longText";
    private static final String LOCATION_TEXT = "locationText";

    RecyclerView mRecyclerView;
    TextView tvLongText, tvLocationText, tvDate, tvPrices;
    LinearLayoutManager mLayoutManager;
    PricesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary700));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvLongText = (TextView) findViewById(R.id.tv_long_text);
        tvLocationText = (TextView) findViewById(R.id.tv_location_text);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvPrices = (TextView) findViewById(R.id.tv_prices);

        Intent intent = getIntent();
        ArrayList<String> prices = intent.getStringArrayListExtra(PRICES);
        ArrayList<String> descriptions = intent.getStringArrayListExtra(DESCRIPTIONS);
        String text = intent.getStringExtra(TEXT);
        String longText = intent.getStringExtra(LONG_TEXT);
        String locationText = intent.getStringExtra(LOCATION_TEXT);
        long date = intent.getLongExtra(DATE, 0);

        if(prices.isEmpty() && descriptions.isEmpty()) tvPrices.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle(text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvLongText.setText(longText);
        tvLocationText.setText(locationText);
        tvDate.setText(performDate(date));

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new PricesRecyclerAdapter(prices, descriptions);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param date: milliseconds
     * @return date in SimpleDateFormat
     */
    public String performDate(long date) {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.US).format(new Date(date));
    }
}
