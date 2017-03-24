package com.dmt.twitterdemo.model;

public class HashtagRowItem
{
    private String time, tag;

    public HashtagRowItem(String time, String tag)
    {
        this.time = time;
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return(tag);
    }
}