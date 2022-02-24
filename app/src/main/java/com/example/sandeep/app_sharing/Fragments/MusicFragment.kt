package com.example.sandeep.app_sharing.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.DATA
import android.provider.MediaStore
import android.provider.SyncStateContract.Columns.DATA
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.MusicAdaptor
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.musicClass
import pl.droidsonroids.gif.GifImageView
import java.lang.Exception

class MusicFragment : Fragment() {

    lateinit var progress: ProgressBar
    lateinit var empty: GifImageView
    lateinit var recyclerView: RecyclerView
    private lateinit var allMusic:ArrayList<musicClass>
    lateinit var MusicAdaptor:MusicAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_music, container, false)
        progress=view.findViewById(R.id.prograss)
        empty = view.findViewById(R.id.empty)
        recyclerView=view.findViewById(R.id.recyclerView)

        empty.visibility=View.INVISIBLE

        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }
        allMusic= ArrayList()

        if (allMusic.isEmpty()){
            progress.visibility=View.VISIBLE
            allMusic=getAllMusic()

            if (allMusic.isNotEmpty()){

                recyclerView.layoutManager=GridLayoutManager(requireContext(),4)
                recyclerView.setHasFixedSize(true)

                MusicAdaptor= MusicAdaptor(requireContext(),allMusic)
                recyclerView.adapter=MusicAdaptor
                progress.visibility=View.INVISIBLE

            }else{
                empty.visibility=View.VISIBLE
            }
        }

        return view
    }

    private fun getAllMusic(): ArrayList<musicClass> {
        val musics = ArrayList<musicClass>()

        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val Projection = arrayOf(MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.Media.DISPLAY_NAME)

        val cursor = requireActivity().contentResolver.query(musicUri,Projection,null,null,null)



        try {
            cursor!!.moveToFirst()

            do {

                val musicobj=musicClass()
                musicobj.musicName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                musicobj.musicPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                musics.add(musicobj)

            } while (cursor.moveToNext())

            cursor.close()

        }catch (e:Exception){

            e.printStackTrace()

        }


        return musics

    }


}