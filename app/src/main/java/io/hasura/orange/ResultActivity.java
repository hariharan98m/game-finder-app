package io.hasura.orange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView notify_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        notify_text= (TextView) findViewById(R.id.notify_text);
        setTitle("Invite to Join");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notify_text.setText(Html.fromHtml("<font color=\"#07c\" ><b>"+getIntent().getStringExtra("name")+"</b></font>, <i>"+getIntent().getStringExtra("mobile")+"</i> has invited you for <br><br><font color=\"#f1085c\" ><b>"+getIntent().getStringExtra("game")+"</b></font> at <i>"+getIntent().getStringExtra("place")+"</i> <br><br>Join and make it a lot more fun"));

    }
}
