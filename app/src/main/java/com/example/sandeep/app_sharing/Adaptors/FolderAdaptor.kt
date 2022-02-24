package com.example.sandeep.app_sharing.Adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.BuildConfig
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.folderClass
import java.io.File

class FolderAdaptor(val context: Context, val allFolder: ArrayList<folderClass>, private val listner:ClickLisner) : RecyclerView.Adapter<FolderAdaptor.viewHolder>() {
    var count=0
    class viewHolder(val view:View):RecyclerView.ViewHolder(view) {
            val Name =view.findViewById<TextView>(R.id.txt)
            val select =view.findViewById<ImageView>(R.id.select)
    }

    interface ClickLisner {
        fun onClick(count:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.folder_model,parent,false)

        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var viewName=allFolder[position]

        holder.Name.text=viewName.folderName

        holder.itemView.setOnClickListener(View.OnClickListener {

            var intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", File(allFolder.get(position).folderPath)
                ));

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setType("application/vnd.android.package-archive")
            context.startActivity(Intent.createChooser(intent,"AppShare"))
//            if (!holder.select.isVisible) {
//                holder.select.visibility = View.VISIBLE
//                count++
//                listner.onClick(count)
//            }else{
//                holder.select.visibility = View.INVISIBLE
//                count--
//                listner.onClick(count)
//            }
        })
    }

    override fun getItemCount(): Int {
        return allFolder.size
    }

}
