package com.example.sty.liteweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.sty.liteweather.gson.DailyForecast;
import com.example.sty.liteweather.gson.HourlyForecast;
import com.example.sty.liteweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String KEY = "ca559894d7c24a7094b04266795f20d7";

    public DrawerLayout mDrawerLayout;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mTitleCity;
    private TextView mTitleUpdateTime;
    private TextView mDegreeText;
    private TextView mWeatherInfoText;
    private LinearLayout mForecastLayout;
    private LinearLayout mHourlyForecastLayout;
    private TextView mComfortText;
    private TextView mCarWashText;
    private TextView mDressClotheText;
    private TextView mFluText;
    private TextView mSportText;
    private TextView mTravelText;
    private TextView mUltravioletText;

    private ArrayList<String> mPermissionList;
    private LocationClient mLocationClient;
    private String mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Toast.makeText(MainActivity.this, "刷新定位", Toast.LENGTH_SHORT).show();
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();

                final String province = bdLocation.getProvince();
                final String city = bdLocation.getCity();
                final String district = bdLocation.getDistrict();

                mPosition = latitude + "," + longitude;
                final String address = "https://free-api.heweather.com/v5/weather?city=" + mPosition + "&key=" + KEY;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(address)
                                .build();
                        String responseString = null;
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            responseString = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final Weather weather = handleWeatherResponse(responseString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("ok".equals(weather.mStatus)) {
                                    Toast.makeText(MainActivity.this, "获取天气数据成功", Toast.LENGTH_SHORT).show();
                                    mTitleCity.setText(province + " " + city + " " + district);
                                    showWeatherInfo(weather);
                                } else {
                                    Toast.makeText(MainActivity.this, "获取天气数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        setContentView(R.layout.activity_weather);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitleCity = (TextView) findViewById(R.id.title_city);
        mTitleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        mDegreeText = (TextView) findViewById(R.id.degree_text);
        mWeatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        mForecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mHourlyForecastLayout = (LinearLayout) findViewById(R.id.hourly_forecast_layout);
        mComfortText = (TextView) findViewById(R.id.comfort_text);
        mCarWashText = (TextView) findViewById(R.id.car_wash_text);
        mDressClotheText = (TextView) findViewById(R.id.dress_text);
        mFluText = (TextView) findViewById(R.id.flu_text);
        mSportText = (TextView) findViewById(R.id.sport_text);
        mTravelText = (TextView) findViewById(R.id.travel_text);
        mUltravioletText = (TextView) findViewById(R.id.uv_text);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mPosition);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mPermissionList = new ArrayList<>();
        havePermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        havePermission(Manifest.permission.READ_PHONE_STATE);
        havePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!mPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, mPermissionList.toArray(new String[mPermissionList.size()]), REQUEST_CODE);
        } else {
            requestClient();
        }
    }

    private void requestWeather(String position) {
        final String address = "https://free-api.heweather.com/v5/weather?city=" + position + "&key=" + KEY;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(address)
                        .build();
                String responseString = null;
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    responseString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Weather weather = handleWeatherResponse(responseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(weather.mStatus)) {
                            Toast.makeText(MainActivity.this, "刷新天气数据成功", Toast.LENGTH_SHORT).show();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(MainActivity.this, "刷新天气数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void havePermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            mPermissionList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int permission :
                        grantResults) {
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "You have to grant all the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                requestClient();
            } else {
                Toast.makeText(this, "You have to grant all the permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestClient() {
        initClient();
        mLocationClient.start();
    }

    private void initClient() {
        LocationClientOption clientOption = new LocationClientOption();
//        clientOption.setScanSpan(5000);
        clientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(clientOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocationClient.stop();
    }

    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showWeatherInfo(Weather weather) {
        String updateTime = weather.mBasic.mUpdate.mUpdateTime.split(" ")[1];
        String degree = weather.mNow.mTemperature + "℃";
        String weatherInfo = weather.mNow.mMore.mInfo;
        mTitleUpdateTime.setText("服务器更新时间：" + updateTime);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);
        mForecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : weather.mDailyDailyForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(dailyForecast.mDate);
            infoText.setText(dailyForecast.mMore.mInfo);
            maxText.setText(dailyForecast.mTemperature.mMax);
            minText.setText(dailyForecast.mTemperature.mMin);
            mForecastLayout.addView(view);
        }
        mHourlyForecastLayout.removeAllViews();
        for (HourlyForecast forecast : weather.mHourlyForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.mDate.split(" ")[1]);
            infoText.setText(forecast.mMore.mInfo);
            maxText.setText(forecast.mTemperature);
            minText.setText(forecast.mTemperature);
            mHourlyForecastLayout.addView(view);
        }

        String comfort = "舒适度指数：" + weather.mSuggestion.mComfort.mBrief + "\n" + weather.mSuggestion.mComfort.mInfo;
        String carWash = "洗车指数：" + weather.mSuggestion.mCarWash.mBrief + "\n" + weather.mSuggestion.mCarWash.mInfo;
        String dressClothe = "穿衣指数：" + weather.mSuggestion.mClothe.mBrief + "\n" + weather.mSuggestion.mClothe.mInfo;
        String sport = "运动指数：" + weather.mSuggestion.mSport.mBrief + "\n" + weather.mSuggestion.mSport.mInfo;
        String travel = "旅游指数：" + weather.mSuggestion.mTravel.mBrief + "\n" + weather.mSuggestion.mTravel.mInfo;
        String flu = "感冒指数：" + weather.mSuggestion.mInfluenza.mBrief + "\n" + weather.mSuggestion.mInfluenza.mInfo;
        String uv = "紫外线指数：" + weather.mSuggestion.mUltraviolet.mBrief + "\n" + weather.mSuggestion.mUltraviolet.mInfo;
        mComfortText.setText(comfort);
        mCarWashText.setText(carWash);
        mDressClotheText.setText(dressClothe);
        mFluText.setText(flu);
        mSportText.setText(sport);
        mTravelText.setText(travel);
        mUltravioletText.setText(uv);
    }
}
