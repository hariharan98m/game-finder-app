package io.hasura.orange.db_conn;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 17-10-2017.
 */

public class AuthReq {
    @SerializedName("name")
    String name;

    @SerializedName("mob")
    String mob;

    @SerializedName("game")
    String game;

    @SerializedName("lat")
    String lat;

    @SerializedName("long")
    String longi;

    @SerializedName("place")
    String place;

    public AuthReq(String name, String mob, String game, String lat, String longi, String place) {
        this.name = name;
        this.mob = mob;
        this.game = game;
        this.lat = lat;
        this.longi = longi;
        this.place = place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
