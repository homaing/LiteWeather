package com.example.sty.liteweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("status")
    public String mStatus;

    @SerializedName("basic")
    public Basic mBasic;

    @SerializedName("now")
    public Now mNow;

    @SerializedName("suggestion")
    public Suggestion mSuggestion;

    @SerializedName("daily_forecast")
    public List<DailyForecast> mDailyDailyForecastList;

    @SerializedName("hourly_forecast")
    public List<HourlyForecast> mHourlyForecastList;
}
