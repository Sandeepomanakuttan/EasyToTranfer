package com.example.sandeep.app_sharing.Fragments

import android.content.ContentValues
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor
import com.example.sandeep.app_sharing.App_Data
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.ViewAdaptor
import java.io.File


class AppFragment(private val listener:ViewAdaptor.clickListner) : Fragment(), ViewAdaptor.clickListner {
    private lateinit var recycle: RecyclerView
    lateinit var apps: ArrayList<App_Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= inflater.inflate(R.layout.fragment_app, container, false)

        recycle = binding.findViewById(R.id.recycler)

        val pm = context?.packageManager
        val packages = pm?.getInstalledApplications(PackageManager.GET_META_DATA)
        apps=ArrayList<App_Data>()

        val list = context?.packageManager?.getInstalledPackages(0)
        val s= (list?.size)?.minus(1)
        for (i in 0..s!!){
            val packageInfo = list[i]
            if (packageInfo!!.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                val appName = packageInfo.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                Log.e("App Name",appName)
                val icon = packageInfo.applicationInfo.loadIcon(requireActivity().packageManager)
                val apkPath = packageInfo.applicationInfo.sourceDir
                val apkSize = File(apkPath).length()
                apps.add(ToJni(appName, apkSize,icon,apkPath))
            }

        }

        //retrive System Files

        for (packageinfo in packages!!) {
            var name= pm.getApplicationLabel(packageinfo) as String
            if (name.isEmpty()){
                name=packageinfo.packageName
                Log.d(ContentValues.TAG, "Package name:" + packageinfo.packageName)

            }
            val icon = pm.getApplicationIcon(packageinfo)
            val apkPath = packageinfo.sourceDir
            val apkSize = File(packageinfo.sourceDir).length()

           // apps.add(ToJni(name, apkSize,icon,apkPath))
        }


        recycle.layoutManager= GridLayoutManager(requireContext(),4)
        recycle.setHasFixedSize(true)

        var adaptor= ViewAdaptor(requireContext(),apps,this)
        recycle.adapter=adaptor

        return binding
    }

    external fun ToJni(name: String, apkSize: Long, icon: Drawable, apkPath: String): App_Data
    // external fun stringToJNI(): String


    companion object {
        // Used to load the 'app_sharing' library on application startup.
        init {
            System.loadLibrary("app_sharing")
        }
    }

    override fun ApponclickPlus() {
        listener.ApponclickPlus()
    }

    override fun ApponclickMinus() {
        listener.ApponclickMinus()
    }
}


