package io.hasura.orange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.hasura.orange.db_conn.SelectFriends;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    List<SelectFriends> sf=new ArrayList<>();
    int ItemCount;
    Activity act;
    public ProgressDialog progressDialog;

    public ArticleViewAdapter(List<SelectFriends> sf) {
        this.sf = sf;
        ItemCount= sf.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.guys_nearby, parent, false);

        return new ArticleViewHolder(view);
    }

    public static double round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        //Set the data
        holder.guy_name.setText(sf.get(position).getYname());
        String lat= sf.get(position).getYlat();
        String longi= sf.get(position).getYlong();

        holder.lat_long.setText(lat.substring(0,5)+" N "+longi.substring(0,5)+" E");
        holder.tick.setVisibility(View.INVISIBLE);
        if(position==0||position==1||position==3||position==4||position==6||position==7)
            holder.tick.setImageResource(R.drawable.tick);
        else
            holder.tick.setImageResource(R.drawable.no);
        new DoIt().execute(holder);
    }

    @Override
    public int getItemCount() {
        return ItemCount;
    }




    private class DoIt extends AsyncTask<ArticleViewHolder, Void, ArticleViewHolder>{

        @Override
        protected ArticleViewHolder doInBackground(ArticleViewHolder... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(ArticleViewHolder articleViewHolder) {
            super.onPostExecute(articleViewHolder);
            articleViewHolder.tick.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



}
