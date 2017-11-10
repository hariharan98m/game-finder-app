package io.hasura.orange;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder{

    public TextView guy_name, lat_long;
    ImageView tick;


    public ArticleViewHolder(View itemView) {
        super(itemView);
        guy_name= (TextView) itemView.findViewById(R.id.guy_name);
        lat_long= (TextView) itemView.findViewById(R.id.lat_long);
        tick=(ImageView) itemView.findViewById(R.id.tick);

    }

}
