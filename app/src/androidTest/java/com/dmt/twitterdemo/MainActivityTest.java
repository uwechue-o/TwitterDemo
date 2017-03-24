package com.dmt.twitterdemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dmt.twitterdemo.model.DataUIModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by o_uwe on 3/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest
{

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_userImageFetch()
    {
        onView(withId(R.id.request_token_button))
                .perform(click());

        DataUIModel userObj = new DataUIModel();
        userObj.setRealName("Dragonball Zee");
        userObj.setRetweetCount("31415927");
        userObj.setTweet("Hello World #newbie");
        userObj.setTwitterName("Goku Kakorot");
        userObj.setUserPicUrl("http://www.pngmart.com/files/3/Dragon-Ball-Z-Characters-PNG-Photos.png");

        mActivityRule.getActivity().fetchUserImage(userObj.getUserPicUrl(), userObj);
    }

}
