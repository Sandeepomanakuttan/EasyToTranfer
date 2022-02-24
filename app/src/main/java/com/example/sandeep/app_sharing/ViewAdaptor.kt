package com.example.sandeep.app_sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor
import com.example.sandeep.app_sharing.Adaptors.GalaryAdaptor.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.Console
import java.io.File
import java.lang.Exception
import java.lang.String.format
import java.util.*
import kotlin.collections.ArrayList

class ViewAdaptor(var context:Context, private val list: List<App_Data>, private val lisner: clickListner): RecyclerView.Adapter<ViewAdaptor.viewHolder>() {
    var hsize:String=""
    lateinit var obj:App_Data
    var listed=ArrayList<App_Data>()
    companion object{
        var count=0;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.app_item,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if (count==0){
            holder.selected.visibility=View.INVISIBLE
        }
        val viewItem=list[position]
        holder.app_Name.text=viewItem.name
        holder.app_icon.setImageDrawable(viewItem.icon)
        holder.app_size.text=getsize(viewItem.app_Size)

        holder.card.setOnClickListener(View.OnClickListener {
//            if (!holder.selected.isVisible) {
//                holder.selected.isVisible = true
//                lisner.ApponclickPlus()
//                obj.apkpath=list.get(position).apkpath
//            }else{
//                holder.selected.isVisible = false
//                lisner.ApponclickMinus()
//            }
//
//            listed.add(obj)

            var intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".provider", File(list.get(position).apkpath)));

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setType("application/vnd.android.package-archive")
            context.startActivity(Intent.createChooser(intent,"AppShare"))
        })
    }



    private fun getsize(appSize: Long): String {

        try {
            if (appSize<1024){
                hsize= Formatter().format(context.getString(R.string.app_size_mib,appSize))
                    .toString()

            }else if(appSize<Math.pow(1024.0, 2.0)){
                hsize= Formatter().format(context.getString(R.string.app_size_kib,(appSize/1024)))
                    .toString()            }
            else if(appSize<Math.pow(1024.0, 3.0)){
                hsize= Formatter().format(context.getString(R.string.app_size_mib,(appSize/Math.pow(1024.0,2.0))))
                    .toString()
            }else{
                hsize= Formatter().format(context.getString(R.string.app_size_gib,(appSize/Math.pow(1024.0,3.0))))
                    .toString()
            }

        }catch (e:Exception){
            Log.e("Tag", "Errroooooor")
        }



        return hsize


    }

    override fun getItemCount(): Int {
      return list.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card:CardView=itemView.findViewById(R.id.app_row)
        val app_icon:CircleImageView=itemView.findViewById(R.id.app_icon)
        val selected:CircleImageView=itemView.findViewById(R.id.select)
        val app_Name=itemView.findViewById(R.id.app_Name) as TextView
        val app_size:TextView=itemView.findViewById(R.id.app_size)

    }
    interface clickListner {
        fun ApponclickPlus()
        fun ApponclickMinus()
    }
}


