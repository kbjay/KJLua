package com.xm.lua;


import android.content.Context;

import com.xm.lua_annotation.LuaInterfaceForClass;

/**
 * 动态修改sp
 */

@LuaInterfaceForClass(luaName = "sp")
public class SPLua {
    public static int getInt(String key){
        return LuaApplication.instance.getSharedPreferences("lua", Context.MODE_PRIVATE).getInt(key,0);
    }

    public static void putInt(String key,int value){
        LuaApplication.instance.getSharedPreferences("lua", Context.MODE_PRIVATE).edit().putInt(key,value).apply();
    }
}
