package com.example.sandeep.app_sharing.Adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.sandeep.app_sharing.BuildConfig
import com.example.sandeep.app_sharing.R
import com.example.sandeep.app_sharing.imageloader.musicClass
import java.io.File

class MusicAdaptor(val context: Context,val mlist:ArrayList<musicClass>):RecyclerView.Adapter<MusicAdaptor.viewHolder>() {
    class viewHolder(view: View) :RecyclerView.ViewHolder(view) {

        val musicName:TextView=view.findViewById(R.id.txt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.music_model,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val music=mlist[position]
        holder.musicName.text=music.musicName

        holder.itemView.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", File(mlist.get(position).musicPath)
                ));

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setType("application/vnd.android.package-archive")
            context.startActivity(Intent.createChooser(intent,"AppShare"))
        }
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}