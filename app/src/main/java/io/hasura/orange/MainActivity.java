package io.hasura.orange;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import io.hasura.orange.db_conn.AuthApiManager;
import io.hasura.orange.db_conn.AuthReq;
import io.hasura.orange.db_conn.CustomResponseListener;
import io.hasura.orange.db_conn.ReqResponse;

public class MainActivity extends BaseActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    EditText mobile;
    TextView intro_text;
    LoginButton button;
    public static void startActivity(Activity startingActivity){
        Intent intent = new Intent(startingActivity, MainActivity.class);
        startingActivity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobile= (EditText) findViewById(R.id.mobile);
        button= (LoginButton) findViewById(R.id.login_button);
        intro_text= (TextView) findViewById(R.id.intro_text);
        intro_text.setText(Html.fromHtml("<font color=\"#07c\" size=\"20\"><b>Orange is LIVE</b></font><br><br> A Place where you Find Gamemates<br><br> Bring Out the Fun in every Game"));
        button.setVisibility(View.INVISIBLE);
        callbackManager = CallbackManager.Factory.create();
        loginButton= (LoginButton) findViewById(R.id.login_button);

        setLoginPermissions();

        if(Profile.getCurrentProfile()!=null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("mob",mobile.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    private boolean validate(String s) {
        if(!s.isEmpty() && s.length()==10)
            return true;
        else
            Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
    }

    private void insert_user_details() {
        new AuthApiManager(this).getApiInterface().insert(new AuthReq(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName(), mobile.getText().toString().trim(), "", "0", "0", ""))
                .enqueue(new CustomResponseListener<ReqResponse>() {
                    @Override
                    public void onSuccessfulResponse(ReqResponse response) {
                        //Toast.makeText(MainActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailureResponse(ReqResponse errorResponse) {
                        //Toast.makeText(HomeActivity.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void setLoginPermissions(){
        loginButton.setReadPermissions("email","public_profile","user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                HomeActivity.startActivity(MainActivity.this,true, "Logged In");
                insert_user_details();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void filter(View v){
        if(validate(mobile.getText().toString()))
            button.setVisibility(View.VISIBLE);

    }

}
