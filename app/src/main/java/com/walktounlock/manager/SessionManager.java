package com.walktounlock.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by M Umer Saleem on 9/8/2017.
 */

public class SessionManager {

    private final String TAG=SessionManager.class.getSimpleName();
    private final String KEY_IS_LOGGED_IN="isLoggedIn";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE=0;
    public static final String KEY_PREF_NAME = "pref_name";
    public static final String KEY_NAME = "name";

    public SessionManager(Context context) {
        pref=context.getSharedPreferences(KEY_PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void setLogin(boolean islogin,String name)
    {
        editor.putBoolean(KEY_IS_LOGGED_IN,islogin);
        editor.putString(KEY_NAME,name);
        editor.commit();
    }
    public String getName()
    {
        return pref.getString(KEY_NAME,"");
    }

    public void setLogout()
    {
        editor.putBoolean(KEY_IS_LOGGED_IN,false);
        editor.putString(KEY_NAME,"");
        editor.commit();
    }

    public boolean isLoggedin()
    {
        return pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }

}
