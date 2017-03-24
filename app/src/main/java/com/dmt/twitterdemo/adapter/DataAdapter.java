package com.dmt.twitterdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmt.twitterdemo.R;
import com.dmt.twitterdemo.model.DataUIModel;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<DataUIModel> dataList;

    public DataAdapter(ArrayList<DataUIModel> dataList) {
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.userName.setText(dataList.get(position).getRealName());
        holder.userHandle.setText(dataList.get(position).getTwitterName());
        holder.userTweet.setText(dataList.get(position).getTweet());
        holder.imgViewIcon.setImageBitmap(dataList.get(position).getUserPic());
        holder.retweets.setText(dataList.get(position).getRetweetCount());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName,userHandle,userTweet, retweets;
        private ImageView imgViewIcon;

        public ViewHolder(View view) {
            super(view);

            userName = (TextView)view.findViewById(R.id.user_name);
            userHandle = (TextView)view.findViewById(R.id.twitter_handle);
            userTweet = (TextView)view.findViewById(R.id.user_tweet);
            imgViewIcon = (ImageView)view.findViewById(R.id.user_picture);
            retweets = (TextView)view.findViewById(R.id.retweet_count);
        }
    }
}
