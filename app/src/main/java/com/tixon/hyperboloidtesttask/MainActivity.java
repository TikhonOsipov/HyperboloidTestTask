package com.tixon.hyperboloidtesttask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class MainActivity extends BaseSpiceActivity {
    private static final String URL = "http://test.boloid.com:9000/tasks";
    private static final String TASKS = "tasks";
    private static final String ID = "ID";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String TEXT = "text";
    private static final String LONG_TEXT = "longText";
    private static final String PRICE = "price";
    private static final String LOCATION_TEXT = "locationText";
    private static final String LOCATION = "location";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String PRICES = "prices";
    private static final String DESCRIPTION = "description";
    private static final String DESCRIPTIONS = "descriptions";

    public ArrayList<Task> tasks = new ArrayList<>();
    public MapView mMapView;
    public MapController mMapController;
    public OverlayManager mOverlayManager;
    public Overlay mOverlay;
    public Drawable pin;
    public boolean isUpdating = false;

    private MyHttpRequest txtRequest;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary700));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        mMapView = (MapView) findViewById(R.id.map);
        mMapController = mMapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        pin = getResources().getDrawable(R.drawable.pin);

        txtRequest = new MyHttpRequest(URL);

        getSpiceManager().execute(txtRequest, "txt", DurationInMillis.ONE_MINUTE,
                new TextRequestListener());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOverlayManager.removeOverlay(mOverlay);
                isUpdating = true;
                txtRequest = new MyHttpRequest(URL); //read JSON data from URL
            }
        });
    }

    public final class TextRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.failure), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {
            parseJSONData(result);
            Log.d("myLogs", result);
        }
    }

    public void parseJSONData(String s) {
        JSONObject dataJsonObj;
        mOverlay = new Overlay(mMapController);

        try {
            dataJsonObj = new JSONObject(s);
            JSONArray JSONArrayTasks = dataJsonObj.getJSONArray(TASKS);

            for(int i = 0; i < JSONArrayTasks.length(); i++) {
                JSONObject JSONObjTask = JSONArrayTasks.getJSONObject(i);
                JSONObject location = JSONObjTask.getJSONObject(LOCATION);
                JSONArray prices = JSONObjTask.getJSONArray(PRICES);

                final Task task = new Task(
                        location.getDouble(LATITUDE),
                        location.getDouble(LONGITUDE),
                        JSONObjTask.getInt(ID),
                        JSONObjTask.getLong(DATE),
                        JSONObjTask.getString(TITLE),
                        JSONObjTask.getString(TEXT),
                        JSONObjTask.getString(LONG_TEXT),
                        JSONObjTask.getString(LOCATION_TEXT)
                );

                for(int j = 0; j < prices.length(); j++) {
                    JSONObject JSONObjPrice = prices.getJSONObject(j);
                    int price = JSONObjPrice.getInt(PRICE);
                    String description = JSONObjPrice.getString(DESCRIPTION);
                    task.addPrice(price, description);
                }

                tasks.add(task);

                GeoPoint geoPoint = new GeoPoint(tasks.get(i).latitude, tasks.get(i).longitude);
                OverlayItem item = new OverlayItem(geoPoint, pin);

                BalloonItem balloonItem = new BalloonItem(getApplicationContext(), geoPoint);
                balloonItem.setText(tasks.get(i).title);
                balloonItem.setOnBalloonListener(new OnBalloonListener() {
                    @Override
                    public void onBalloonViewClick(BalloonItem balloonItem, View view) {
                        GeoPoint mGeoPoint = balloonItem.getGeoPoint();
                        int taskNumber = findTask(balloonItem.getText().toString(),
                                mGeoPoint.getLat(),
                                mGeoPoint.getLon());
                        Task mTask = tasks.get(taskNumber);

                        //create intent for moving into DetailsActivity
                        Intent intentDetails =
                                new Intent(getApplicationContext(), DetailsActivity.class);
                        //put extras in intentDetails
                        intentDetails.putStringArrayListExtra(PRICES, mTask.prices);
                        intentDetails.putStringArrayListExtra(DESCRIPTIONS, mTask.descriptions);
                        intentDetails.putExtra(TEXT, mTask.text);
                        intentDetails.putExtra(LONG_TEXT, mTask.longText);
                        intentDetails.putExtra(DATE, mTask.date);
                        intentDetails.putExtra(LOCATION_TEXT, mTask.locationText);

                        startActivity(intentDetails);
                    }

                    @Override
                    public void onBalloonShow(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonHide(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationStart(BalloonItem balloonItem) {

                    }

                    @Override
                    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

                    }
                });

                item.setBalloonItem(balloonItem);
                mOverlay.addOverlayItem(item);
            }

            mOverlayManager.addOverlay(mOverlay);
            GeoPoint geoPoint = new GeoPoint(tasks.get(0).latitude, tasks.get(0).longitude);
            mMapController.setPositionAnimationTo(geoPoint);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param title: title of task
     * @param lat: latitude
     * @param lon: longitude
     * @return position of task with params in tasks ArrayList
     */
    public int findTask(String title, double lat, double lon) {
        int index = 0;
        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).title.equals(title) && tasks.get(i).latitude == lat &&
                    tasks.get(i).longitude == lon) {
                index = i;
                break;
            }
        }
        return index;
    }
}
