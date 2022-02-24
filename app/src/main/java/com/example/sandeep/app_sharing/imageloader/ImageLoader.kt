package com.example.sandeep.app_sharing.imageloader

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ImageLoader {
    companion object{
        fun listOfImages(context: Context):ArrayList<String>{
            var projection:Array<String>
            var listofAllImages:ArrayList<String>
            var uri:Uri
           uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
           projection= arrayOf(MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            var orderBy = MediaStore.Video.Media.DATE_TAKEN
            var cursor=context.contentResolver.query(uri,projection,null,null,orderBy + "DESC")

            var col_index_data=cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            //var col_index_folderName=cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            listofAllImages= ArrayList()
            while(cursor!!.moveToNext()){
               var absolutePath = col_index_data?.let { cursor.getString(it) }
                if (absolutePath != null) {
                    listofAllImages.add(absolutePath)
                }
            }

            return listofAllImages
        }
    }
}