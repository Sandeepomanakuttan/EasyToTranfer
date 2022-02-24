package com.example.sandeep.app_sharing

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor
import com.example.sandeep.app_sharing.Fragments.*
import com.example.sandeep.app_sharing.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayout

class Main : AppCompatActivity(), FolderFragment.CountLisner,GalaryAdaptor.ClickListner,ViewAdaptor.clickListner {


    lateinit var binding: ActivityMain2Binding
    lateinit var tab:TabLayout
    lateinit var viewPager:ViewPager
    lateinit var textView:TextView
    var counter: Int=0
    var count: Int=0
    var c: Int=0

    override fun onBackPressed() {
        var alertDialog=AlertDialog.Builder(this)
        alertDialog.setMessage("Return Home Page")
        alertDialog.setPositiveButton("yes") { dialoge: DialogInterface, which ->
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        setTabs()
    }

    private fun initialize() {
        tab=binding.tab
        viewPager=binding.viewPager
        textView=binding.textView
    }

    private fun setTabs() {
        val  adaptor=FragmentAdaptor(supportFragmentManager)
        adaptor.addFragment(RecentItemFragment())
        adaptor.addFragment(AppFragment(this))
        adaptor.addFragment(FolderFragment(this))
        adaptor.addFragment(ImageFragment(this))
        adaptor.addFragment(VideoFragment())
        adaptor.addFragment(MusicFragment())

        viewPager.adapter=adaptor
        tab.setupWithViewPager(viewPager)

        tab.getTabAt(0)!!.setIcon(R.drawable.ic_recent)
        tab.getTabAt(1)!!.setIcon(R.drawable.ic_app)
        tab.getTabAt(2)!!.setIcon(R.drawable.ic_folder)
        tab.getTabAt(3)!!.setIcon(R.drawable.ic_image)
        tab.getTabAt(4)!!.setIcon(R.drawable.ic_video)
        tab.getTabAt(5)!!.setIcon(R.drawable.ic_music)

    }

    override fun onCounter(count: Int) {
       var c=this.count+count
        textView.text=c.toString()
    }

    override fun onclickPlus() {
        count++
        textView.text=count.toString()
    }

    override fun onclickMinus() {
        count--
        textView.text=count.toString()

    }

    override fun ApponclickPlus() {
        count++
        textView.text=count.toString()
    }

    override fun ApponclickMinus() {
        count--
        textView.text=count.toString()
    }
}