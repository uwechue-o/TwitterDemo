package com.dmt.twitterdemo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ListAdapter;

/**
 * Created by o_uwe on 3/23/2017.
 */

public class DialogSpinner extends AppCompatSpinner {

    public DialogSpinner(Context context) {
        super(context);
    }

    public DialogSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean performClick() {
        new AlertDialog.Builder(getContext()).setAdapter((ListAdapter) getAdapter(),
                (dialog,which)->{setSelection(which); dialog.dismiss();})
                .create()
                .show();
        return true;
    }
}
