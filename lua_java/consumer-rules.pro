# 保留luajava下的所有类
-keep class com.xm.luajava.* {*;}

## 保留Gson
-keep class com.google.gson.Gson {*;}
-keep class com.xiaomi.gson.Gson {*;}

-keep @com.xm.lua_annotation.LuaInterfaceForObj class * {*;}
-keep @com.xm.lua_annotation.LuaInterfaceForClass class * {*;}
