package com.dmt.twitterdemo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dmt.twitterdemo.R;
import com.dmt.twitterdemo.model.HashtagRowItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by o_uwe on 3/23/2017.
 */
public class HashtagArrayAdapter extends ArrayAdapter<HashtagRowItem>
{
    private final Activity context;
    private final HashtagRowItem[] values;

    /*
    private class ViewHolder
    {
        TextView timeView,tagVIew;
    }

    ViewHolder viewHolder;
    */

    public HashtagArrayAdapter(Activity context, int textViewResourceId, HashtagRowItem[] values)
    {
        super(context, textViewResourceId, values);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.values = values;
    }

    public int getCount(){
        return values.length;
    }

    public HashtagRowItem getItem(int position){

        return(values[position]);
    }

    public View getView(int position, View view, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.hashtag_db_row, null,true);

        TextView time = (TextView) rowView.findViewById(R.id.hashtag_time);
        TextView tag = (TextView) rowView.findViewById(R.id.hashtag_text);

        time.setText(values[position].getTime());
        tag.setText(values[position].getTag());

        return rowView;
    }
}
