package com.example.sandeep.app_sharing.Fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor
import com.example.sandeep.app_sharing.Adaptors.VideoAdaptor
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.videoClass
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception


class VideoFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private lateinit var allVideo:ArrayList<videoClass>
    lateinit var VideoAdaptor: VideoAdaptor
    lateinit var prograss: ProgressBar
    lateinit var empty: GifImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        recyclerView=view.findViewById(R.id.recyclerView)
        prograss=view.findViewById(R.id.prograss)
        empty=view.findViewById(R.id.empty)
        empty.visibility=View.INVISIBLE

        if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }

        allVideo= ArrayList()

        if (allVideo.isEmpty()){
            prograss.visibility=View.VISIBLE
            allVideo=getAllVideo()

            if (allVideo.isNotEmpty()){

                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager=GridLayoutManager(requireContext(),2)
                VideoAdaptor=VideoAdaptor(requireContext(),allVideo)
                recyclerView.adapter=VideoAdaptor
                prograss.visibility=View.INVISIBLE

            }else{
                empty.visibility=View.VISIBLE
            }
        }

        return view
    }

    private fun getAllVideo(): ArrayList<videoClass> {
        val videos = ArrayList<videoClass>()

        val allVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA,MediaStore.Video.VideoColumns.DISPLAY_NAME)

        var cursor = requireActivity().contentResolver.query(allVideoUri,projection,null,null,null)

        try {
            cursor!!.moveToFirst()
            do{
                val videoObj = videoClass()
                videoObj.videoName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                videoObj.videoPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videos.add(videoObj)
            }while (cursor.moveToNext())
            cursor.close()

        }
        catch (e:Exception){
            e.printStackTrace()
        }

        return videos
    }


}