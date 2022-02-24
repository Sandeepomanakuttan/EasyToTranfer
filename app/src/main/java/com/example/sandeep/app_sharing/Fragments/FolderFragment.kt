package com.example.sandeep.app_sharing.Fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.FolderAdaptor
import com.example.sandeep.app_sharing.Adaptors.MusicAdaptor
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.folderClass
import com.example.sandeep.app_sharing.imageloader.musicClass
import pl.droidsonroids.gif.GifImageView

class FolderFragment(val countLis:CountLisner) : Fragment(),FolderAdaptor.ClickLisner {

    lateinit var progress: ProgressBar
    lateinit var empty: GifImageView
    lateinit var recyclerView: RecyclerView
    private lateinit var allFolder:ArrayList<folderClass>
    lateinit var txt:TextView
    private var curentDir=Environment.getExternalStorageDirectory()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_folder, container, false)

        progress=view.findViewById(R.id.prograss)
        empty = view.findViewById(R.id.empty)
        recyclerView=view.findViewById(R.id.recyclerView)
        txt=view.findViewById(R.id.txt)

        empty.visibility=View.INVISIBLE

        allFolder= ArrayList()

        if (allFolder.isEmpty()) {
            progress.visibility=View.VISIBLE
            allFolder = fileLoader()

            for (i in 0 until allFolder.size){
                Log.e("Folder", allFolder[i].folderName.toString())
            }

            if (allFolder.isNotEmpty()){

                recyclerView.layoutManager=GridLayoutManager(requireContext(),4)
                recyclerView.adapter= FolderAdaptor(requireActivity(),allFolder,this)
                progress.visibility=View.INVISIBLE
            }else{

                empty.visibility=View.VISIBLE

            }
        }
        return view
    }

    private fun fileLoader(): ArrayList<folderClass> {
        var folders = ArrayList<folderClass>()
            var file=""
        for (obj in curentDir.listFiles()) {
            val folderObj=folderClass()
                if (obj.isDirectory && !obj.isHidden) {
                   folderObj.folderName= file+"\n"+obj.name
                    folderObj.folderPath=obj.path
                    folders.add(folderObj)
                }else{
                    folderObj.folderName= file+"\n"+obj.name
                    folderObj.folderPath=obj.path
                    folders.add(folderObj)
                }

        }
        return folders
    }

    companion object

    override

    override fun onClick(count: Int) {

        countLis.onCounter(count)
    }

    interface CountLisner {
        fun onCounter(count: Int)
    }
}


