package com.lhy.comm.mars;

import android.content.Context;
import android.os.Environment;

import com.lhy.comm.BuildConfig;
import com.lhy.comm.utils.ProcessUtil;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * Created by luohy on 2018/8/7.
 */

public class XLogUtil {

    public static void initXlog(Context context){
        openXlog();
    }

    private static  void openXlog() {
//        String processName = ProcessUtil.getMyProcessName();
//        if (processName == null) {
//            return;
//        }
//
//        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
//        final String logPath = SDCARD + "/DemoCommunity/log";
//        String logFileName = processName.indexOf(":") == -1 ? "DemoCommunity" : ("DemoCommunity_" + processName.substring(processName.indexOf(":") + 1));
//        if (BuildConfig.DEBUG) {
//            Xlog.appenderOpen(Xlog.LEVEL_VERBOSE, Xlog.AppednerModeAsync, "", logPath, logFileName, "");
//            Xlog.setConsoleLogOpen(true);
//        } else {
//            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, "", logPath, logFileName, "");
//            Xlog.setConsoleLogOpen(false);
//        }
//
//        Log.setLogImp(new Xlog());
    }

    public static void closeXlog() {
//        Log.appenderClose();
    }

    //================================  my Log Util ================================
//
//    public static void openMyLog(){
//        String processName = getMyProcessName();
//        if (processName == null) {
//            return;
//        }
//
//        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String logFileName = processName.indexOf(":") == -1 ? "DemoCommunity" : ("DemoCommunity_" + processName.substring(processName.indexOf(":") + 1));
//        final String logPath = SDCARD + "/DemoComm/log/" + logFileName + ".log";
//
//        openMyLog(logPath);
//    }
//
//    public static void d(String tag, String msg){
//        writeMyLog(tag, msg);
//    }
//
//    private static void writeMyLog(String tag, String msg){
//        writeMyLog(0, tag, msg);
//    }
//    public native static void openMyLog(String path);
//    public native static void writeMyLog(int level, String tag, String msg);

    static {
//        System.loadLibrary("stlport_shared");
//        System.loadLibrary("marsxlog");
    }

}
