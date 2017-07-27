package com.example.sty.liteweather.gson;

import com.google.gson.annotations.SerializedName;

public class HourlyForecast {
    @SerializedName("date")
    public String mDate;

    @SerializedName("tmp")
    public String mTemperature;

    @SerializedName("cond")
    public HourlyForecast.More mMore;

    public class More {

        @SerializedName("txt")
        public String mInfo;

    }
}
