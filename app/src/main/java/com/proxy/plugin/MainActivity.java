package com.proxy.plugin;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geekmu.sproxy.SPluginIntent;
import com.geekmu.sproxy.SPluginManager;


public class MainActivity extends AppCompatActivity {
    private Button btnStartPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pluginApkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DynamicLoadHost/plugin-debug.apk";
        SPluginManager.getInstance(this).loadApk(pluginApkPath);

        btnStartPlugin = (Button)findViewById(R.id.button);
        btnStartPlugin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPluginManager pluginManager = SPluginManager.getInstance(MainActivity.this);
                pluginManager.startPluginActivity(MainActivity.this, new SPluginIntent("com.sp.plugin", "com.sp.plugin.MainActivity"));
            }
        });
    }
}
