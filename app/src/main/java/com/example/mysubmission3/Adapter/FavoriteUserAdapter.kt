package com.example.mysubmission3.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission3.R
import com.example.mysubmission3.db.UserFavorite
import com.example.mysubmission3.helper.NoteDiffCallback
import com.example.mysubmission3.ui.activity.UserDetailActivity
import kotlinx.android.synthetic.main.item_row_usersfavorite.view.*

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteListViewHolder>() {
    private val listFavoriteUser = ArrayList<UserFavorite>()

    fun setListFavorite(listFavoriteUser: List<UserFavorite>) {
        val diffCallback = NoteDiffCallback(this.listFavoriteUser, listFavoriteUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listFavoriteUser)
        diffResult.dispatchUpdatesTo(this)
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
                list_favorite.setOnClickListener{
                    val intent = Intent(it.context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_NOTE, favorite)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}