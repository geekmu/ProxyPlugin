package com.geekmu.sproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by geekmu on 2017/6/23.
 */

public interface ISPLifeCycle {
    public void onCreate(Bundle savedInstanceState);
    public void onStart();
    public void onRestart();
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestroy();
    public void attach(Activity proxyActivity , SPluginPackage spluginPackage);
}
