package io.hasura.orange.db_conn;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 17-10-2017.
 */

public class ReqResponse {
    @SerializedName("message")
    String message;

    @SerializedName("error")
    String error;

    public ReqResponse(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}
