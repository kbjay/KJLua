package com.xm.lua_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解决混淆问题
 * 从Source 改为Class：
 * 为什么是source：apt是在预编译阶段生成java文件，注解无需带到class文件中，source就够了
 * 为什么改成了class：混淆是在class到dex这个阶段，为了保证被注解标识的类不被混淆，所以需要在class文件中包含该注解
 * 不修改的话 -keep @com.xm.lua_annotation.LuaInterfaceForObj class * {*;} 无效
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface LuaInterfaceForClass {
    String luaName();
}
