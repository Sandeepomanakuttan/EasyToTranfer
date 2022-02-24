package com.example.sandeep.app_sharing

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.databinding.ActivityMainBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.lang.Byte.compare
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ViewAdaptor.clickListner {

    override fun onBackPressed() {
        finish()
    }
    val delay = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txt=findViewById<TextView>(R.id.txt);

        txt.animate().apply {
            duration=1000
            rotationYBy(360f)
        }.start()


        Handler().postDelayed(Runnable {


                var intent=Intent(this,Main::class.java)
                startActivity(intent)
                finish()


        }, delay.toLong())




    }



    /**
     * A native method that is implemented by the 'app_sharing' native library,
     * which is packaged with this application.
     */
    external fun To(name: String, apkSize: Long, icon: Drawable, apkPath: String): App_Data
    // external fun stringToJNI(): String


    companion object {
        // Used to load the 'app_sharing' library on application startup.
        init {
            System.loadLibrary("app_sharing")
        }
    }



    override fun ApponclickPlus() {
        TODO("Not yet implemented")
    }

    override fun ApponclickMinus() {
        TODO("Not yet implemented")
    }
}