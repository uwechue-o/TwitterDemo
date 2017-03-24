package com.dmt.twitterdemo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ouwechue on 3/16/17.
 */

public class OAuthToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }
}
