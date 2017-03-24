package com.dmt.twitterdemo.model;

import java.util.List;

/**
 * Created by ouwechue on 3/18/17.
 *
 * This class models the top-level nodes of interest in the twitter JSON response.
 *
 * This is the overall JSON node structure we're interested in extracting from the Twitter response:
 *
 * {
 *    "statuses":  [
 *    {
 *        "id_str": "xxxxxxx",
 *        "text": "xxxxxxx",
 *        "user":  {
 *            "name": "xxxxxxx",
 *            "screen_name": "xxxxxxx",
 *            "location": "xxxxxxx",
 *            "profile_background_image_url": "http://xxxxxxx.jpg"
 *        }
 *    },
 *    {
 *        "id_str": "yyyyyyyyy",
 *        "text": "yyyyyyyyy",
 *        "user":  {
 *            "name": "yyyyyyyyy",
 *            "screen_name": "yyyyyyyyy",
 *            "location": "yyyyyyyyy",
 *            "profile_background_image_url": "http://yyyyyyyyy.jpg"
 *        }
 *    },
 *    {
 *        "id_str": "zzzzzzzzzz",
 *        "text": "zzzzzzzzzz",
 *        "user":  {
 *            "name": "zzzzzzzzzz",
 *            "screen_name": "zzzzzzzzzz",
 *            "location": "zzzzzzzzzz",
 *            "profile_background_image_url": "http://zzzzzzzzzz.jpg"
 *        }
 *    }
 *    ]
 * }
 */

public class UserModel_parentNode
{
    // nested node data
    private List<UserModel_childNode> statuses;


    public List<UserModel_childNode> getStatuses()
    {
        return statuses;
    }

    public void setStatuses(List<UserModel_childNode> s)
    {
        this.statuses = s;
    }
}
