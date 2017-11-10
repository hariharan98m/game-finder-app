package io.hasura.orange.db_conn;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 18-10-2017.
 */

public class SelectFriends {
    @SerializedName("yname")
    String yname;

    @SerializedName("fname")
    String fname;

    @SerializedName("fmob")
    String fmob;
    @SerializedName("ymob")
    String ymob;
    @SerializedName("game")
    String game;
    @SerializedName("place")
    String place;

    @SerializedName("flat")
    String flat;
    @SerializedName("flong")
    String flong;

    @SerializedName("ylat")
    String ylat;

    @SerializedName("ylong")
    String ylong;

    public String getYname() {
        return yname;
    }

    public String getYmob() {
        return ymob;
    }

    public String getGame() {
        return game;
    }

    public String getPlace() {
        return place;
    }

    public String getFlat() {
        return flat;
    }

    public String getFlong() {
        return flong;
    }

    public String getYlat() {
        return ylat;
    }

    public String getYlong() {
        return ylong;
    }

    public String getFname() {
        return fname;
    }

    public String getFmob() {
        return fmob;
    }
}
