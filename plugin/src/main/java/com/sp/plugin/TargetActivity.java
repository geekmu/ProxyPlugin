package com.sp.plugin;

import android.os.Bundle;

import com.geekmu.sproxy.SPluginBaseActivity;

public class TargetActivity extends SPluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.activity_target);
    }
}
