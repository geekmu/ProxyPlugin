package com.geekmu.sproxy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SPluginBaseActivity extends Activity implements ISPLifeCycle {

    protected Activity that;
    protected Activity mProxyActivity;
    protected SPluginManager mPluginManager;
    protected SPluginPackage mPluginPackage;
    protected int mFrom = SPConstants.FROM_INTERNAL;

    /**
     * 连接代理和插件
     * @param proxyActivity
     * @param spluginPackage
     */
    @Override
    public void attach(Activity proxyActivity , SPluginPackage spluginPackage) {
        mProxyActivity = proxyActivity;
        that = mProxyActivity;
        mPluginPackage = spluginPackage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mFrom = savedInstanceState.getInt(SPConstants.FROM, 0);
        }

        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
            that = mProxyActivity;
        }

        mPluginManager = SPluginManager.getInstance(that);
    }

    @Override
    public void setContentView(View view) {
        mProxyActivity.setContentView(view);
    }

    @Override
    public void onStart() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onRestart();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onResume();
        }

    }

    @Override
    public void onPause() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onPause();
        }

    }

    @Override
    public void onStop() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onStop();
        }

    }

    @Override
    public void onDestroy() {
        if(mFrom == SPConstants.FROM_INTERNAL) {
            super.onDestroy();
        }

    }

    public View findViewById(int id) {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.findViewById(id):this.mProxyActivity.findViewById(id);
    }
    public Intent getIntent() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getIntent():this.mProxyActivity.getIntent();
    }

    public ClassLoader getClassLoader() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getClassLoader():this.mProxyActivity.getClassLoader();
    }

    public Resources getResources() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getResources():this.mProxyActivity.getResources();
    }

    public String getPackageName() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getPackageName():this.mPluginPackage.packageName;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getLayoutInflater():this.mProxyActivity.getLayoutInflater();
    }

    public MenuInflater getMenuInflater() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getMenuInflater():this.mProxyActivity.getMenuInflater();
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getSharedPreferences(name, mode):this.mProxyActivity.getSharedPreferences(name, mode);
    }

    public Context getApplicationContext() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getApplicationContext():this.mProxyActivity.getApplicationContext();
    }

    public WindowManager getWindowManager() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getWindowManager():this.mProxyActivity.getWindowManager();
    }

    public Window getWindow() {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getWindow():this.mProxyActivity.getWindow();
    }

    public Object getSystemService(String name) {
        return this.mFrom == SPConstants.FROM_INTERNAL?super.getSystemService(name):this.mProxyActivity.getSystemService(name);
    }

    public void finish() {
        if(this.mFrom == SPConstants.FROM_INTERNAL) {
            super.finish();
        } else {
            this.mProxyActivity.finish();
        }

    }
    public int startPluginActivity(SPluginIntent dlIntent) {
        return this.startPluginActivityForResult(dlIntent, -1);
    }

    public int startPluginActivityForResult(SPluginIntent dlIntent, int requestCode) {
        if(mFrom == SPConstants.FROM_EXTERNAL && dlIntent.getPluginPackage() == null) {
            dlIntent.setPluginPackage(mPluginPackage.packageName);
        }

        return mPluginManager.startPluginActivityForResult(that, dlIntent, requestCode);
    }

}
