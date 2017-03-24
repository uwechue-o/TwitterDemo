package com.dmt.twitterdemo.model;

import android.graphics.Bitmap;

/**
 * Created by ouwechue on 3/18/17.
 */

public class DataUIModel
{
    private String twitterName;
    private String realName;
    private String tweet;
    private Bitmap userPic;
    private String retweetCount;
    private String userPicUrl;

    public String getTwitterName() {
        return twitterName;
    }

    public String getRealName() {
        return realName;
    }

    public String getTweet() {
        return tweet;
    }

    public Bitmap getUserPic() {
        return userPic;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }


    public void setTwitterName(String s)
    {
        twitterName = s;
    }

    public void setRealName(String s)
    {
        realName = s;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public void setUserPic(Bitmap b)
    {
        userPic = b;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(String retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setUserPicUrl(String s){
        this.userPicUrl = s;
    }
}
