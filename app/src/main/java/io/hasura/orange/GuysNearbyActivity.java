package io.hasura.orange;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import io.hasura.orange.db_conn.AuthApiManager;
import io.hasura.orange.db_conn.AuthReq;
import io.hasura.orange.db_conn.CustomResponseListener;
import io.hasura.orange.db_conn.ReqResponse;
import io.hasura.orange.db_conn.SelectFriends;

import static io.hasura.orange.R.id.coordinatorLayout;

public class GuysNearbyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button closeConnection;
    CoordinatorLayout coordinatorLayout;
    static List<SelectFriends> sf= new ArrayList<>();

    public static void startActivity(Activity startingActivity, List<SelectFriends> sfriends){
        Intent intent = new Intent(startingActivity, GuysNearbyActivity.class);
        sf= sfriends;
        startingActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guys_nearby);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout1);
        closeConnection= (Button) findViewById(R.id.close_conn);
        closeConnection.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                closeConn();
            }
        });
        setTitle("Guys Nearby");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.guys_nearby);
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSnackbar("Loading Friends Peek");
            }
        },500);

        //Step 1--Set the adapter
        recyclerView.setAdapter(new ArticleViewAdapter(sf));

        //Step2 -- Set the layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeConn();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        closeConn();
    }

    private void closeConn() {
        new AuthApiManager(this).getApiInterface().update_game_place(new AuthReq(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName(), "", "", "", "", ""))
                .enqueue(new CustomResponseListener<ReqResponse>() {
                    @Override
                    public void onSuccessfulResponse(ReqResponse response) {
                        Toast.makeText(GuysNearbyActivity.this, "Game Connection Closed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailureResponse(ReqResponse errorResponse) {
                        Toast.makeText(GuysNearbyActivity.this, errorResponse.getError()+ " :Game reset failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>" + s + "</i></font>"), Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(GuysNearbyActivity.this, R.color.blue));
        snackbar.show();
    }
}
