package com.lhy.comm

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import com.lhy.comm.mars.XLogUtil
import com.lhy.comm.ui.PermissionCheckActivity
import com.lhy.comm.utils.OsUtil
import com.tencent.mars.xlog.Xlog
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.support.v4.content.FileProvider
import android.os.Build
import com.lhy.comm.mars.MyLogUtil
import com.lhy.comm.test.APITest


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        redirectToPermissionCheckIfNeeded(this)
//        startActivity(Intent(this, PermissionCheckActivity::class.java))

        MyLogUtil.openMyLog()
        val tv = findViewById<TextView>(R.id.sample_text)
        tv.setOnClickListener(View.OnClickListener {
            tv.setText("U clicked ")
            val msg = "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"+ "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"+ "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"+ "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"            + "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"+ "U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈U clicked 哈哈"
            val msg2 = msg + msg + msg + msg + msg + msg + msg + msg + msg
            MyLogUtil.d("MainActivity", msg2)
        })

//        APITest.testUriAndFileProvider(this)
    }

    /**
     * Check if the activity needs to be redirected to permission check
     * @return true if [Activity.finish] was called because redirection was performed
     */
    fun redirectToPermissionCheckIfNeeded(activity: Activity): Boolean {
        if (!OsUtil.hasRequiredPermissions()) {
            startActivity(Intent(this, PermissionCheckActivity::class.java))
        } else {
            // No redirect performed
            return false
        }

        // Redirect performed
        activity.finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
//        XLogUtil.closeXlog()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
//            System.loadLibrary("native-lib")
        }
    }
}
