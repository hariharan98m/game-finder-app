package io.hasura.orange;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class BaseActivity extends AppCompatActivity{
    public AlertDialog AD;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog= new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

    }

    public void showProgressDialog(Boolean shouldShow) {
        if (shouldShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void showDotProgressDialog(String msg, Boolean shouldshow){
        if(!shouldshow)
            AD.dismiss();
        else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dot_progress_bar, null);
            TextView text = (TextView) dialogView.findViewById(R.id.progress_text);
            text.setText(msg);
            dialogBuilder.setView(dialogView);
            AD = dialogBuilder.create();
            AD.setCancelable(false);
            if (shouldshow)
                AD.show();
        }


    }
}
