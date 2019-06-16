package com.walktounlock.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by umer on 28-Feb-18.
 */

public class LockManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE=0;
    public static final String KEY_PREF_NAME = "pref_name";

    public LockManager(Context context) {
        pref=context.getSharedPreferences(KEY_PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

}
