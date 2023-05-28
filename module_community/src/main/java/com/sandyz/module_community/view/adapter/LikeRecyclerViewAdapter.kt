package com.sandyz.module_community.view.adapter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandyz.alltimers.common.bean.User
import com.sandyz.module_community.R

//点赞横向拖动列表的adapter很简单
class LikeRecyclerViewAdapter(private val fragment: Fragment?, private var mList: List<User>) :
    RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder>() {

    private var mLayoutInflater = LayoutInflater.from(fragment?.context)


    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {

        when(viewType){
            1->return ViewHolder(mLayoutInflater.inflate(R.layout.item_like_account, container, false))
        }
        return ViewHolder(mLayoutInflater.inflate(R.layout.item_talk_big, container, false))
    }

    override fun getItemCount(): Int {

        return mList.size

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textViewUsrName?.text = mList[position].nickname
        //ImageLoader.with(mContext).load(mList[position].picUrl).into(holder.imageViewUsrPic)
        holder.itemView.setOnClickListener {
            // TODO 头像被点击
        }

    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewUsrName: TextView? = itemView.findViewById<TextView>(R.id.textViewUsrName)
        val imageViewUsrPic: ImageView? = itemView.findViewById<ImageView>(R.id.imageViewUsrPic)

    }

}