package com.dmt.twitterdemo.model;

import android.util.Log;

/**
 * Created by ouwechue on 3/18/17.
 *
 * This class models the first-level nested nodes of interest in the twitter JSON response.
 *
 * It is designed to easily allow a collection of such objects be sorted by some other class.
 *
 */

public class UserModel_childNode implements Comparable<UserModel_childNode>
{
    // top-level nodes:
    private String id_str;          // tweet ID
    private String text;            // the tweet
    private String retweet_count;   // number of retweets

    // nested nodes
    private UserModel_grandchildNode user;

    // GETTERS
    public String getId_str() {
        return id_str;
    }

    public String getText() {
        return text;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public UserModel_grandchildNode getUser()
    {
        return user;
    }

    // SETTERS
    public void setId_str(String s)
    {
        this.id_str = s;
    }

    public void setText(String s)
    {
        this.text = s;
    }

    public void setRetweet_count(String s){
        this.retweet_count = s;
    }

    public void setUser(UserModel_grandchildNode u)
    {
        this.user = u;
    }

    // COMPARATOR
    public int compareTo(UserModel_childNode user)
    {
        int thisCount = 0;
        int otherCount = 0;

        try {
            thisCount = Integer.parseInt(getRetweet_count());
            otherCount = Integer.parseInt(user.getRetweet_count());
        }catch(NumberFormatException numex){
            Log.e("Comparator", "Error " + numex.getMessage());
        }

        return (otherCount - thisCount);
    }
}
