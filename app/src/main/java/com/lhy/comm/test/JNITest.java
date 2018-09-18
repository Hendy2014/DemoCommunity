package com.lhy.comm.test;

/**
 * Created by luohy on 2018/9/4.
 */

public class JNITest {

    public native static String MyNewString();
    public native static String MyNewGlobalString();
    public native static String MyNewWeakGlobalRefString();
}
