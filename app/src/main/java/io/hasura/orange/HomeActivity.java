package io.hasura.orange;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import io.hasura.orange.db_conn.AuthApiManager;
import io.hasura.orange.db_conn.AuthReq;
import io.hasura.orange.db_conn.CustomResponseListener;
import io.hasura.orange.db_conn.ReqResponse;
import io.hasura.orange.db_conn.SelectFriends;

public class HomeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , LocationListener {
    GoogleApiClient mGoogleApiClient;

    public static void startActivity(Activity startingActivity, boolean login, String s) {
        Intent intent = new Intent(startingActivity, HomeActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("snack", s);
        startingActivity.startActivity(intent);
        startingActivity.finish();
    }

    CoordinatorLayout coordinatorLayout;
    TextView hi, dev;
    EditText place, game;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Start a Game");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        final boolean login = getIntent().getBooleanExtra("login", false);
        hi = (TextView) findViewById(R.id.invite_sent);
        place = (EditText) findViewById(R.id.where_game);
        game = (EditText) findViewById(R.id.what_game);
        dev = (TextView) findViewById(R.id.dev);

        dev.setMovementMethod(LinkMovementMethod.getInstance());

        dev.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(HomeActivity.this);
            }
        });
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();

        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (login)
                    showSnackbar(getIntent().getStringExtra("snack").toString().trim());
                hi.setText("Hi " + Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName() + ",");
            }
        },500);


    }



    protected void createLocationRequest() {
        mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(1000*60*5);
        mLocationRequest.setFastestInterval(1000*60*5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i("Latitude",Double.toString(location.getLatitude()));
            Log.i("Longitude",Double.toString(location.getLongitude()));
            new AuthApiManager(this).getApiInterface().update_lat_long(new AuthReq(Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName(),"","",Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),""))
                    .enqueue(new CustomResponseListener<ReqResponse>() {
                        @Override
                        public void onSuccessfulResponse(ReqResponse response) {
                             Toast.makeText(HomeActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailureResponse(ReqResponse errorResponse) {
                            Toast.makeText(HomeActivity.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    });
            new AuthApiManager(this).getApiInterface().select_game_starter(new AuthReq(Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName(),"","","","",""))
                    .enqueue(new CustomResponseListener<SelectFriends>() {
                        @Override
                        public void onSuccessfulResponse(SelectFriends response) {
                            notify_this_idiot(response);
                        }

                        @Override
                        public void onFailureResponse(ReqResponse errorResponse) {
                            //Toast.makeText(HomeActivity.this, "Nobody has started a game or is nearby", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    private void notify_this_idiot(SelectFriends friend) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.invite)
                        .setContentTitle("You have somebody in store waiting for you to join")
                        .setContentText("Invite to Join");
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("name",friend.getFname());
        resultIntent.putExtra("game",friend.getGame());
        resultIntent.putExtra("place",friend.getPlace());
        resultIntent.putExtra("lat",friend.getFlat());
        resultIntent.putExtra("long",friend.getFlong());
        resultIntent.putExtra("mobile",friend.getFmob());

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        // Sets an ID for the notification
            int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }




    public Target target;
    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.logout_button, menu);

        item = menu.findItem(R.id.profile_pic);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                item.setIcon(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(HomeActivity.this).load(Profile.getCurrentProfile().getProfilePictureUri(100, 100)).transform(new CircleTransform()).into(target);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile_pic:
                Uri number = Uri.parse("tel:");
                Intent callIntent = new Intent(Intent.ACTION_VIEW, number);
                startActivity(callIntent);
                return true;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                MainActivity.startActivity(HomeActivity.this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>" + s + "</i></font>"), Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
        snackbar.show();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    public void start_guys_nearby(View v) {
        if (validate(place.getText().toString(), game.getText().toString())) {
            new AuthApiManager(this).getApiInterface().update_game_place(new AuthReq(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName(), "", game.getText().toString(), "", "", place.getText().toString()))
                    .enqueue(new CustomResponseListener<ReqResponse>() {
                        @Override
                        public void onSuccessfulResponse(final ReqResponse response) {
                            Toast.makeText(HomeActivity.this, "Game and Place Set", Toast.LENGTH_SHORT).show();
                            new AuthApiManager(HomeActivity.this).getApiInterface().select_friends(new AuthReq(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName(), "","","","",""))
                                    .enqueue(new CustomResponseListener<List<SelectFriends>>() {
                                        @Override
                                        public void onSuccessfulResponse(List<SelectFriends> response) {
                                            GuysNearbyActivity.startActivity(HomeActivity.this, response);
                                        }

                                        @Override
                                        public void onFailureResponse(ReqResponse errorResponse) {
                                            Toast.makeText(HomeActivity.this, "No friend found in distance", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onFailureResponse(ReqResponse errorResponse) {
                            Toast.makeText(HomeActivity.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private boolean validate(String s, String s1) {
        if (s.isEmpty()) {
            Toast.makeText(this, "Can't leave the Place blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (s1.isEmpty()) {
            Toast.makeText(this, "Game can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    final int ACCESS_FINE_LOCATION=1998;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("OnConnect","Connected");
        findLocation();
    }

    public void findLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(HomeActivity.this, "Please add permissions to continue", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                Log.i("Request permission","asked");
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
            }
            return;
        }
        stopLocationUpdates();

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ACCESS_FINE_LOCATION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findLocation();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("",connectionResult.getErrorMessage());
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

}
