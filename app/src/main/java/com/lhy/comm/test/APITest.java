package com.lhy.comm.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

import javax.validation.MessageInterpolator;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by luohy on 2018/8/30.
 */

public class APITest {
    public static void testUriAndFileProvider(Context context){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File savePhoto = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "/DCIM/Camera/"+System.currentTimeMillis() + ".png");
        //老的写法，在Android7.0以上会出现FileUriExposedException
//        val uri = Uri.fromFile(File("file:///sdcard/Pictures/1.png"))
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        Uri contentUri = FileProvider.getUriForFile(context, "com.lhy.comm.fileprovider", savePhoto);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(intent);
    }
}
