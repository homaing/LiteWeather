package com.example.sty.liteweather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("comf")
    public Comfort mComfort;

    @SerializedName("cw")
    public CarWash mCarWash;

    @SerializedName("sport")
    public Sport mSport;

    @SerializedName("drsg")
    public Clothe mClothe;

    @SerializedName("flu")
    public Influenza mInfluenza;

    @SerializedName("trav")
    public Travel mTravel;

    @SerializedName("uv")
    public Ultraviolet mUltraviolet;

    public class Comfort {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class CarWash {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class Sport {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class Clothe {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class Influenza {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class Travel {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }

    public class Ultraviolet {
        @SerializedName("brf")
        public String mBrief;

        @SerializedName("txt")
        public String mInfo;
    }
}
