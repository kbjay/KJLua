package com.xm.lua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xm.lua_inject.LuaExecutor;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tv_content);

        findViewById(R.id.bt_sp).setOnClickListener(view -> {
            String execute = LuaExecutor.execute(readAssetsTxt(MainActivity.this, "sp.lua"));
            tv.setText(execute);
        });
        findViewById(R.id.bt_permission).setOnClickListener(view -> {
            String execute = LuaExecutor.execute(readAssetsTxt(MainActivity.this, "permission.lua"));
            tv.setText(execute);
        });
        findViewById(R.id.bt_db).setOnClickListener(view -> {
            String execute = LuaExecutor.execute(readAssetsTxt(MainActivity.this, "db.lua"));
            tv.setText(execute);
        });

        findViewById(R.id.bt_call_obj_method).setOnClickListener(view -> {
            String execute = LuaExecutor.execute(readAssetsTxt(MainActivity.this, "callObjMethod.lua"));
            tv.setText(execute);
        });
    }


    public static String readAssetsTxt(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "err";
    }
}