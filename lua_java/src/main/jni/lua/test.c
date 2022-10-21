#include <stdio.h>
#include <jni.h>

#include "lua.h"
#include "lauxlib.h"
#include "lualib.h"
#include "log.h"


static int test_hello(lua_State *L) {
    printf("hello lua");
    return 0;
}

static int fn1(lua_State *L,JNIEnv * env ) {
//    Q:怎么获取env？？？？ 怎么根据类名调用到指定的java方法？？？？
//    jclass clazz= (*env)->FindClass(env,lua_tostring(L, 1));

    lua_pushnumber(L,1);
    lua_gettable(L,1);
    const char *name1 = lua_tostring(L, -1);
    printf("%s",name1);
    LOGW(8,"%s", name1);
    lua_pop(L,1);

    lua_pushnumber(L,2);
    lua_gettable(L,1);
    const char *name2 = lua_tostring(L, -1);
    printf("%s",name2);
    LOGW(8,"%s", name2);
    lua_pop(L,1);

    lua_pushstring(L,"fn1");
    return 1;
}

static const luaL_Reg testLib[] = {
        {"hello", test_hello},
        {"fn1",   fn1},
        {NULL,    NULL}
};

int luaopen_test(lua_State *L) {
    luaL_newlib(L, testLib);
    return 1;
}

