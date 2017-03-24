package com.dmt.twitterdemo.model;

/**
 * Created by ouwechue on 3/18/17.
 *
 * This class models the second-level nested nodes of interest in the twitter JSON response
 */

public class UserModel_grandchildNode
{
    private String name;            // user real name       : /user/name
    private String screen_name;     // user Twitter name    : /user/screen_name
    private String location;        // user's location      : /user/location
    private String profile_image_url;      // user's picture URL  : /user/profile_image_url

    // GETTERS
    public String getName() { return name; }

    public String getScreen_name() { return screen_name; }

    public String getLocation() { return location; }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    // SETTERS
    public void setName(String n)
    {
        this.screen_name = n;
    }

    public void setScreen_name(String s)
    {
        this.screen_name = s;
    }

    public void setLocation(String l)
    {
        this.location = l;
    }

    public void setProfile_image_url(String i)
    {
        this.profile_image_url = i;
    }

}
