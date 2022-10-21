package com.xm.lua;

import android.app.Application;
import android.content.Context;

public class LuaApplication extends Application {

    public static Application instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }
}
