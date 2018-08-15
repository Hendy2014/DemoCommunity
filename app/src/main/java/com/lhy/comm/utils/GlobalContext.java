package com.lhy.comm.utils;

import android.content.Context;

/**
 * Created by luohy on 2018/8/9.
 */

public class GlobalContext {
    private static Context gContext = null;

    public static Context getApplicationContext(){
        return gContext;
    }

    public static void setContext(Context c){
        gContext = c;
    }

}
