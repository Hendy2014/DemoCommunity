package com.lhy.comm;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Trace;
//import android.util.Log;

import com.lhy.comm.mars.XLogUtil;
import com.lhy.comm.utils.GlobalContext;
import com.tencent.mars.xlog.Log;

/**
 * Created by luohy on 2018/8/7.
 */

public class CommApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CommApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Trace.beginSection("app.onCreate");
        super.onCreate();

        GlobalContext.setContext(this.getApplicationContext());
        XLogUtil.initXlog(this);

        Log.d(TAG, "Application onCreate");
        Thread.setDefaultUncaughtExceptionHandler(this);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LogUtil.e(TAG, "LeakCanary.install");
//        LeakCanary.install(this);

        Trace.endSection();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "Application.onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "Application.onLowMemory");
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        Log.e(TAG, "Uncaught exception in thread " + thread);
        Log.e(TAG, android.util.Log.getStackTraceString(ex));
    }

    //TODO: call this when user exit app
    public void onExitApp(){
        Log.appenderClose();
    }

}
