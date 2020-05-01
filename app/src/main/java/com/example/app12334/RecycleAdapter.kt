package com.example.app12334

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_layout.view.*


class RecycleAdapter(val context:Context,val movies:MutableList<Movie>,val itemClickListener: OnItemClickListener) :RecyclerView.Adapter<RecycleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return movies.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val movie =movies[position]

        holder.bind(movie,itemClickListener)
    }
    inner class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(mve:Movie?,action: OnItemClickListener){
            itemView.movietitle.text =mve!!.title
            itemView.releasedate.text=mve!!.releasedate
            itemView.overview.text=mve!!.overview
            Picasso.get().load(mve!!.url).into(itemView.poster)
            itemView.moreinfo.text = "moreInfo"
            itemView.setOnClickListener{action.onItemClicked(mve!!.id,adapterPosition)
            }

        }
    }
}



interface OnItemClickListener{
    fun onItemClicked(id:Int,position: Int)
}