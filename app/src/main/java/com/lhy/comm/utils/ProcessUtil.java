package com.lhy.comm.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

/**
 * Created by luohy on 2018/8/30.
 */

public class ProcessUtil {
    public static String getMyProcessName() {
        int pid = Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager) GlobalContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        return processName;
    }
}
