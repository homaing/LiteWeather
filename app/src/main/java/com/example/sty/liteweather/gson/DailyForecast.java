package com.example.sty.liteweather.gson;

import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    @SerializedName("date")
    public String mDate;

    @SerializedName("tmp")
    public Temperature mTemperature;

    @SerializedName("cond")
    public More mMore;

    public class Temperature {

        @SerializedName("max")
        public String mMax;

        @SerializedName("min")
        public String mMin;

    }

    public class More {

        @SerializedName("txt_d")
        public String mInfo;

    }

}
