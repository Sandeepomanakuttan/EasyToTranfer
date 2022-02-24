package com.example.sandeep.app_sharing.Fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.imageClass
import java.lang.Exception


class ImageFragment(val click: GalaryAdaptor.ClickListner) : Fragment(),GalaryAdaptor.ClickListner {


    lateinit var recyclerView: RecyclerView
    lateinit var allPicture:ArrayList<imageClass>
    lateinit var galaryAdaptor: GalaryAdaptor
    lateinit var prograss:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        prograss = view.findViewById(R.id.prograss)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
        allPicture= ArrayList()

        if (allPicture.isEmpty()){

            prograss.visibility=View.VISIBLE
            allPicture=getAllImage()
            if (allPicture.isNotEmpty()) {
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                recyclerView.setHasFixedSize(true)
                galaryAdaptor = GalaryAdaptor(requireContext(), allPicture,this)
                recyclerView.adapter = galaryAdaptor
                prograss.visibility = View.GONE
            }else{
                Toast.makeText(requireContext(), "image null", Toast.LENGTH_SHORT).show()
            }


        }



        return view;
    }


    private fun getAllImage(): ArrayList<imageClass> {

        var images= ArrayList<imageClass>()

        val allimageUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)


        var cursor = requireActivity().contentResolver.query(allimageUri,projection,null,null,null)

        try {
            cursor!!.moveToFirst()
            do {
                val imageobj= imageClass()
                imageobj.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                imageobj.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                images.add(imageobj)
            }while (cursor.moveToNext())
            cursor.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return images
    }

    override fun onclickPlus() {
        click.onclickPlus()
    }

    override fun onclickMinus() {
        click.onclickMinus()
    }


}


