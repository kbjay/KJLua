package com.xm.lua;

import android.content.pm.PackageManager;
import android.os.Build;

import com.xm.lua_annotation.LuaInterfaceForClass;

/**
 * 判断用户权限
 */
@LuaInterfaceForClass(luaName = "permission")
public class PermissionLua {
    public static String getPermissions() {
        StringBuilder sbDenied = new StringBuilder();
        StringBuilder sbAgreed = new StringBuilder();
        try {
            String[] requestedPermissions = LuaApplication.instance.getPackageManager().getPackageInfo(LuaApplication.instance.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
            for (int i = 0; i < requestedPermissions.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (LuaApplication.instance.checkSelfPermission(requestedPermissions[i]) == PackageManager.PERMISSION_DENIED) {
                        sbDenied.append(" D: ").append(requestedPermissions[i]);
                    } else {
                        sbAgreed.append(" A: ").append(requestedPermissions[i]);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return sbAgreed.append(sbDenied).toString();
    }
}
