package com.lhy.comm.mars;

import android.os.Environment;

import com.lhy.comm.utils.*;

/**
 * Created by luohy on 2018/8/30.
 */
public class MyLogUtil {
    public static void openMyLog(){
        String processName = ProcessUtil.getMyProcessName();
        if (processName == null) {
            return;
        }

        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        String logFileName = processName.indexOf(":") == -1 ? "DemoCommunity" : ("DemoCommunity_" + processName.substring(processName.indexOf(":") + 1));
        final String logPath = SDCARD + "/DemoComm/log/" + logFileName + ".log";
        FileUtils.makeDirs(logPath);

        openMyLog(logPath);
    }

    public static void d(String tag, String msg){
        writeMyLog(tag, msg);
    }

    private static void writeMyLog(String tag, String msg){
        writeMyLog(0, tag, msg);
    }
    public native static void openMyLog(String path);
    public native static void writeMyLog(int level, String tag, String msg);

    static {
        System.loadLibrary("native-lib");
    }
}
