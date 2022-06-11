package com.example.mysubmission3.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.Activity.UserDetailActivity
import com.example.mysubmission3.CustomOnItemClickListener
import com.example.mysubmission3.Data.UserFavorite
import com.example.mysubmission3.R
import kotlinx.android.synthetic.main.item_row_usersfavorite.view.*

class FavoriteUserAdapter(private val Activity: Activity) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteListViewHolder>() {
    var listFavoriteUser = ArrayList<UserFavorite>()
    @SuppressLint("NotifyDataSetChanged")
    set(listFavorite){
        if (listFavorite.size > 0){
            this.listFavoriteUser.clear()
        }
        this.listFavoriteUser.addAll(listFavorite)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(ViewGroup: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val view = LayoutInflater.from(ViewGroup.context).inflate(R.layout.item_row_usersfavorite, ViewGroup, false)
        return FavoriteListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    inner class FavoriteListViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(favorite: UserFavorite){
            with(itemView){
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .apply(RequestOptions().override(250,250))
                    .into(img_item_photo)
                tv_item_username.text = favorite.username
                tv_item_company.text = favorite.company
                tv_item_location.text = favorite.location
                tv_item_caption_company.text = "company :"
                tv_item_caption_location.text = "location :"
                itemView.setOnClickListener{
                    CustomOnItemClickListener(
                        adapterPosition,object: CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(Activity, UserDetailActivity::class.java)
                                intent.putExtra(UserDetailActivity.EXTRA_NOTE, favorite)
                                intent.putExtra(UserDetailActivity.EXTRA_POSITION, position)
                                Activity.startActivity(intent)
                            }
                        }
                    )
                }
            }
        }
    }
}