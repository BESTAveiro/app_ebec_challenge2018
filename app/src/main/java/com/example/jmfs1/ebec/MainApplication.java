package com.example.jmfs1.ebec;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarContext;

/**
 * Created by root on 3/5/17.
 */

public class MainApplication extends Application
{
    static Application me;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;

        // This sets the database persistence (even offline it will work)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SugarContext.init(getContext());
    }

    public static Context getContext()
    {
        return me;
    }
}
