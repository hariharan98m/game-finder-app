package io.hasura.orange.db_conn;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public abstract class CustomResponseListener<T> implements Callback<T> {

    public abstract void onSuccessfulResponse(T response);

    public abstract void onFailureResponse(ReqResponse errorResponse);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            onSuccessfulResponse(response.body());
        }
        else{
            try {
                String errorMessage = response.errorBody().string();
                try{
                    ReqResponse errorResponse = new Gson().fromJson(errorMessage, ReqResponse.class);
                    onFailureResponse(errorResponse);
                }catch (JsonSyntaxException jsonSyntaxException){
                    jsonSyntaxException.printStackTrace();
                    onFailureResponse(new ReqResponse("JSON Syntax Exception"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                onFailureResponse(new ReqResponse("IOException caught"));
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailureResponse(new ReqResponse("You are not connected to the Internet"));
    }
}
