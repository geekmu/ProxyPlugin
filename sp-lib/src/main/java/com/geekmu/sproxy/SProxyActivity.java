package com.geekmu.sproxy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.reflect.Constructor;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SProxyActivity extends Activity{

    protected ISPLifeCycle mISPLifeCycle;

    private String mPackageName;
    private String mClass;
    private SPluginPackage mSPluginPackage;
    private SPluginManager mPluginManager;

    private AssetManager mAssetManager;
    private Resources mResources;
    private ClassLoader mClassLoader;

    private Resources.Theme mTheme;
    private ActivityInfo mActivityInfo;
    private Activity mProxyActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProxyActivity = this;
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra(SPConstants.EXTRA_PACKAGE);
        mClass = intent.getStringExtra(SPConstants.EXTRA_CLASS);

        mPluginManager = SPluginManager.getInstance(this);
        mSPluginPackage = mPluginManager.getPackage(mPackageName);
        mAssetManager = mSPluginPackage.assetManager;
        mResources = mSPluginPackage.resources;
        mClassLoader = mSPluginPackage.classLoader;
        mTheme = mResources.newTheme();

        initializeActivityInfo();
        handleActivityInfo();
        launchPluginActivity();
    }

    private void initializeActivityInfo() {
        PackageInfo packageInfo = mSPluginPackage.packageInfo;
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            if (mClass == null) {
                mClass = packageInfo.activities[0].name;
            }

            //Finals 修复主题BUG
            int defaultTheme = packageInfo.applicationInfo.theme;
            for (ActivityInfo a : packageInfo.activities) {
                if (a.name.equals(mClass)) {
                    mActivityInfo = a;
                    // Finals ADD 修复主题没有配置的时候插件异常
                    if (mActivityInfo.theme == 0) {
                        if (defaultTheme != 0) {
                            mActivityInfo.theme = defaultTheme;
                        } else {
                            if (Build.VERSION.SDK_INT >= 14) {
                                mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                            } else {
                                mActivityInfo.theme = android.R.style.Theme;
                            }
                        }
                    }
                }
            }

        }
    }

    private void handleActivityInfo() {
        if (mActivityInfo.theme > 0) {
            mProxyActivity.setTheme(mActivityInfo.theme);
        }
        Resources.Theme superTheme = mProxyActivity.getTheme();
        mTheme = mResources.newTheme();
        mTheme.setTo(superTheme);
        // Finals适配三星以及部分加载XML出现异常BUG
        try {
            mTheme.applyStyle(mActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: handle mActivityInfo.launchMode here in the future.
    }

    protected void launchPluginActivity() {
        try {
            Class<?> localClass = getClassLoader().loadClass(mClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            mISPLifeCycle = (ISPLifeCycle) instance;
            mISPLifeCycle.attach(mProxyActivity , mSPluginPackage);

            Bundle bundle = new Bundle();
            bundle.putInt(SPConstants.FROM, SPConstants.FROM_EXTERNAL);
            mISPLifeCycle.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mISPLifeCycle.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        //mISPLifeCycle.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        //mISPLifeCycle.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        //mISPLifeCycle.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //mISPLifeCycle.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        //mISPLifeCycle.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //mISPLifeCycle.onDestroy();
        super.onDestroy();
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @Override
    public ClassLoader getClassLoader() {
        return mClassLoader;
    }


}
