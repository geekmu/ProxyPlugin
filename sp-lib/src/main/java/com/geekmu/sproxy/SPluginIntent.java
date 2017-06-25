package com.geekmu.sproxy;

import android.content.Intent;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SPluginIntent extends Intent {
    private String mPluginPackage;
    private String mPluginClass;

    public SPluginIntent(String pluginPackage, String pluginClass) {
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = pluginClass;
    }

    public SPluginIntent(String pluginPackage, Class<?> clazz) {
        this.mPluginPackage = pluginPackage;
        this.mPluginClass = clazz.getName();
    }

    public String getPluginPackage() {
        return mPluginPackage;
    }

    public void setPluginPackage(String pluginPackage) {
        this.mPluginPackage = pluginPackage;
    }

    public String getPluginClass() {
        return mPluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.mPluginClass = pluginClass;
    }
}
