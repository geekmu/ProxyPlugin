package com.geekmu.sproxy;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SPluginPackage {
    public String packageName;
    public String defaultActivity;
    public DexClassLoader classLoader;
    public AssetManager assetManager;
    public Resources resources;
    public PackageInfo packageInfo;

    public SPluginPackage(DexClassLoader loader, Resources resources,
                          PackageInfo packageInfo) {
        this.packageName = packageInfo.packageName;
        this.classLoader = loader;
        this.assetManager = resources.getAssets();
        this.resources = resources;
        this.packageInfo = packageInfo;

        defaultActivity = parseDefaultActivityName();
    }

    private final String parseDefaultActivityName() {
        if (packageInfo.activities != null && packageInfo.activities.length > 0) {
            return packageInfo.activities[0].name;
        }
        return "";
    }
}
