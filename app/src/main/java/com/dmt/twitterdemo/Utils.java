package com.dmt.twitterdemo;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dmt.twitterdemo.model.UserModel_childNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by ouwechue on 3/19/17.
 */

public class Utils
{
    /**
     * sets the text colour of a snackbar object
     *
     * @param bar
     * @param col
     */
    public static void setSnackbarTextColour(Snackbar bar, int col)
    {
        View view = bar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(col);
    }

    /**
     * Eliminate duplicate entries from a collection, while preserving the original data order
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
    {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Eliminate duplicate entries from a collection, while preserving the original data order
     *
     * @param nonUniques
     * @return
     */
    public static List<UserModel_childNode> distinctByKey(List<UserModel_childNode> nonUniques)
    {
        List<UserModel_childNode> uniques = new ArrayList<>();
        Set<String> twitterHandle = new HashSet<>();

        for(UserModel_childNode node : nonUniques)
        {
            try {
                if (twitterHandle.add(node.getUser().getScreen_name())) {
                    uniques.add(node);
                }
            }catch(NullPointerException npex){ /* just skip this iteration and continue looping */}
        }

        return(uniques);
    }

    /**
     * Hide the softkeyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Uses a regexp to perform string escaping
     *
     * @param str
     * @return
     */
    public static String escape(String str)
    {
        Pattern escaper = Pattern.compile("([^a-zA-Z0-9 ])");
        String partiallyEscaped = escaper.matcher(str).replaceAll("\\\\$1");
        String fullyEscaped = partiallyEscaped.replaceAll("\'","\''");
        return (fullyEscaped);
    }
}
