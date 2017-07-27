package com.example.sty.liteweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String mCityName;

    @SerializedName("id")
    public String mWeatherId;

    @SerializedName("update")
    public Update mUpdate;

    public class Update {

        @SerializedName("loc")
        public String mUpdateTime;

    }

}
