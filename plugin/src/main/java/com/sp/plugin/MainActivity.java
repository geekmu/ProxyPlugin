package com.sp.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.geekmu.sproxy.SPluginBaseActivity;
import com.geekmu.sproxy.SPluginIntent;


public class MainActivity extends SPluginBaseActivity {

    private Button btnPlugin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        btnPlugin = (Button)findViewById(R.id.btnPlugin);
        btnPlugin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPluginIntent intent = new SPluginIntent(getPackageName(), TargetActivity.class);
                startPluginActivity(intent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
