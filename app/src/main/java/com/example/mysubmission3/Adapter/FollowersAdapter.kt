package com.example.mysubmission3.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.R
import com.example.mysubmission3.db.Data.User
import kotlinx.android.synthetic.main.item_row_users.view.*

class FollowersAdapter(private val FollowerslistUser: ArrayList<User>) : RecyclerView.Adapter<FollowersAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_users, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(FollowerslistUser[position])
    }

    override fun getItemCount(): Int = FollowerslistUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(user: User){
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(100,100))
                    .into(img_item_photo)
                tv_item_username.text = user.username
                tv_item_id.text = user.id
                tv_item_type.text = user.type
                tv_item_caption_id.text = "id :"
                tv_item_caption_type.text = "type :"
                }
            }
        }
    }
