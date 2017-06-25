package com.geekmu.sproxy;

/**
 * Created by geekmu on 2017/6/23.
 */

public class SPConstants {

    public static final String FROM = "extra.from";
    public static final int FROM_INTERNAL = 0;//插件内部调用
    public static final int FROM_EXTERNAL = 1;//外部加载插件

    public static final int START_RESULT_SUCCESS = 0;
    public static final int START_RESULT_NO_PKG = 1;
    public static final int START_RESULT_NO_CLASS = 2;
    public static final int START_RESULT_TYPE_ERROR = 3;

    public static final String EXTRA_CLASS = "extra.class";
    public static final String EXTRA_PACKAGE = "extra.package";

}
