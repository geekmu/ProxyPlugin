package com.geekmu.sproxy;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SPluginManager {
    private static final String TAG = "SPluginManager";

    private static SPluginManager mSPluginManager;
    private static Context mContext;
    private final HashMap<String, SPluginPackage> mPackagesHolder = new HashMap<String, SPluginPackage>();
    private int mFrom = SPConstants.FROM_INTERNAL;



    public SPluginManager(Context context){
        mContext = context;
    }
    public static SPluginManager getInstance(Context context){
        if(mSPluginManager == null){
            synchronized (SPluginManager.class){
                mSPluginManager = new SPluginManager(context);
            }
        }
        return mSPluginManager;
    }

    public SPluginPackage loadApk(String dexPath){
        this.mFrom = SPConstants.FROM_EXTERNAL;
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(dexPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return null;
        }
        SPluginPackage SPluginPackage = preparePluginEnv(packageInfo, dexPath);
        return SPluginPackage;
    }

    private SPluginPackage preparePluginEnv(PackageInfo packageInfo, String dexPath) {
        SPluginPackage SPluginPackage = null;
        DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
        AssetManager assetManager = createAssetManager(dexPath);
        Resources resources = createResources(assetManager);
        // create pluginPackage
        SPluginPackage = new SPluginPackage(dexClassLoader, resources, packageInfo);
        mPackagesHolder.put(packageInfo.packageName, SPluginPackage);
        return SPluginPackage;
    }

    private String dexOutputPath;

    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, dexOutputPath, mContext.getClassLoader());
        return loader;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    public int startPluginActivity(Context context, SPluginIntent SPluginIntent) {
        return startPluginActivityForResult(context, SPluginIntent, -1);
    }

    public int startPluginActivityForResult(Context context, SPluginIntent SPluginIntent, int requestCode) {

        if(this.mFrom == SPConstants.FROM_INTERNAL) {
            SPluginIntent.setClassName(context, SPluginIntent.getPluginClass());
            this.performStartActivityForResult(context, SPluginIntent, requestCode);
            return 0;
        }
        String packageName = SPluginIntent.getPluginPackage();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("disallow null packageName.");
        }

        SPluginPackage SPluginPackage = mPackagesHolder.get(packageName);
        if (SPluginPackage == null) {
            return SPConstants.START_RESULT_NO_PKG;
        }

        final String className = getPluginActivityFullPath(SPluginIntent, SPluginPackage);
        Class<?> clazz = loadPluginClass(SPluginPackage.classLoader, className);
        if (clazz == null) {
            return SPConstants.START_RESULT_NO_CLASS;
        }

        // get the proxy activity class, the proxy activity will launch the
        // plugin activity.
        Class<? extends Activity> activityClass = getProxyActivityClass();
        if (activityClass == null) {
            return SPConstants.START_RESULT_TYPE_ERROR;
        }

        // put extra data
        SPluginIntent.putExtra(SPConstants.EXTRA_CLASS, className);
        SPluginIntent.putExtra(SPConstants.EXTRA_PACKAGE, packageName);
        SPluginIntent.setClass(mContext, activityClass);
        performStartActivityForResult(context, SPluginIntent, requestCode);
        return SPConstants.START_RESULT_SUCCESS;
    }

    private String getPluginActivityFullPath(SPluginIntent SPluginIntent, SPluginPackage SPluginPackage) {
        String className = SPluginIntent.getPluginClass();
        className = (className == null ? SPluginPackage.defaultActivity : className);
        if (className.startsWith(".")) {
            className = SPluginIntent.getPluginPackage() + className;
        }
        return className;
    }

    private Class<?> loadPluginClass(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clazz;
    }

    private Class<? extends Activity> getProxyActivityClass() {
        Class<? extends Activity> activityClass = null;
        activityClass = SProxyActivity.class;
        return activityClass;
    }

    private void performStartActivityForResult(Context context, SPluginIntent SPluginIntent, int requestCode) {
        Log.d(TAG, "launch " + SPluginIntent.getPluginClass());
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(SPluginIntent, requestCode);
        } else {
            context.startActivity(SPluginIntent);
        }
    }

    public SPluginPackage getPackage(String packageName) {
        return mPackagesHolder.get(packageName);
    }
}
