package com.dmt.twitterdemo;

import com.dmt.twitterdemo.model.UserModel_childNode;
import com.dmt.twitterdemo.model.UserModel_grandchildNode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Various unit tests
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsTest {

    @Test
    public void escape_utility_verify() throws Exception
    {
        String input = "@Future's looking very-bright #1234'0987' it's true. :-) !";
        String output = "\\@Future\\''s looking very\\-bright \\#1234\\''0987\\'' it\\''s true\\. \\:\\-\\) \\!";
        String escaped = Utils.escape(input);

        assertEquals(output, escaped);
    }

    /**
     * test if data will be purged of duplicate nodes(based on Twitter handle) while still preserving original order of data
     *
     * @throws Exception
     */
    @Test
    public void datasort_verify() throws Exception
    {
        List<UserModel_childNode> input = new ArrayList<>(),
                                  output = new ArrayList<>();

        UserModel_grandchildNode g1 = new UserModel_grandchildNode(),
                                g2 = new UserModel_grandchildNode(),
                                g3 = new UserModel_grandchildNode(),
                                g4 = new UserModel_grandchildNode(),
                                g5 = new UserModel_grandchildNode(),
                                g6 = new UserModel_grandchildNode(),
                                g7 = new UserModel_grandchildNode(),
                                g8 = new UserModel_grandchildNode();

        g1.setLocation("Washington DC"); g1.setName("isaac newton"); g1.setScreen_name("inewton"); g1.setProfile_image_url("http://abc.def.com");
        g2.setLocation("Washington DC"); g2.setName("michael faraday"); g2.setScreen_name("mfaraday"); g2.setProfile_image_url("http://abc.def.com");
        g3.setLocation("Washington DC"); g3.setName("michael faraday"); g3.setScreen_name("mfaraday"); g3.setProfile_image_url("http://abc.def.com");
        g4.setLocation("Washington DC"); g4.setName("edward witten"); g4.setScreen_name("ewitten"); g4.setProfile_image_url("http://abc.def.com");
        g5.setLocation("Washington DC"); g5.setName("isaac newton"); g5.setScreen_name("inewton"); g5.setProfile_image_url("http://abc.def.com");
        g6.setLocation("Washington DC"); g6.setName("albert einstein"); g6.setScreen_name("aeinstein"); g6.setProfile_image_url("http://abc.def.com");
        g7.setLocation("Washington DC"); g7.setName("albert einstein"); g7.setScreen_name("aeinstein"); g7.setProfile_image_url("http://abc.def.com");
        g8.setLocation("Washington DC"); g8.setName("edward witten"); g8.setScreen_name("ewitten"); g8.setProfile_image_url("http://abc.def.com");

        UserModel_childNode c1 = new UserModel_childNode(),
                            c2 = new UserModel_childNode(),
                            c3 = new UserModel_childNode(),
                            c4 = new UserModel_childNode(),
                            c5 = new UserModel_childNode(),
                            c6 = new UserModel_childNode(),
                            c7 = new UserModel_childNode(),
                            c8 = new UserModel_childNode();

        c1.setId_str("1023"); c1.setRetweet_count("600"); c1.setText("this is a tweet test"); c1.setUser(g1);
        c2.setId_str("0463"); c2.setRetweet_count("550"); c2.setText("this is a tweet test"); c2.setUser(g2);
        c3.setId_str("2049"); c3.setRetweet_count("500"); c3.setText("this is a tweet test"); c3.setUser(g3);
        c4.setId_str("0935"); c4.setRetweet_count("450"); c4.setText("this is a tweet test"); c4.setUser(g4);
        c5.setId_str("3105"); c5.setRetweet_count("400"); c5.setText("this is a tweet test"); c5.setUser(g5);
        c6.setId_str("8856"); c6.setRetweet_count("350"); c6.setText("this is a tweet test"); c6.setUser(g6);
        c7.setId_str("1773"); c7.setRetweet_count("300"); c7.setText("this is a tweet test"); c7.setUser(g7);
        c8.setId_str("5092"); c8.setRetweet_count("250"); c8.setText("this is a tweet test"); c8.setUser(g8);

        input.add(c1); input.add(c2); input.add(c3); input.add(c4); input.add(c5); input.add(c6); input.add(c7); input.add(c8);

        output.add(c1); output.add(c2); output.add(c4); output.add(c6);

        List<UserModel_childNode> uniques = Utils.distinctByKey(input);

        assertEquals(4, uniques.size());
        assertEquals("1023",uniques.get(0).getId_str());    // isaac newton
        assertEquals("0463",uniques.get(1).getId_str());    // michael faraday
        assertEquals("0935",uniques.get(2).getId_str());    // edward witten
        assertEquals("8856",uniques.get(3).getId_str());    // albert einstein

    }
}