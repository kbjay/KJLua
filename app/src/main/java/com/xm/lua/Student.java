package com.xm.lua;


import com.xm.lua_annotation.LuaInterfaceForObj;

@LuaInterfaceForObj(luaName = "stu")
public class Student {
    public Student() {
    }

    public String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
