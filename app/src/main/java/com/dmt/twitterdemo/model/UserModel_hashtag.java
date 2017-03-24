package com.dmt.twitterdemo.model;

/**
 * Created by ouwechue on 3/18/17.
 *
 * This convenience class models the first-level nested nodes of interest in the twitter JSON response.
 *
 * It is a simplification of the @see com.dmt.twitterdemo.model.UserModel_childNode and is used for fetching basic hashtag data
 *
 */
public class UserModel_hashtag
{
    private String timestamp;       // time of data creation
    private String tag;            // the hashtag

    public UserModel_hashtag(String time, String tag) {
        this.timestamp = time;
        this.tag = tag;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public String getTag() {
        return tag;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}
