package com.example.mysubmission3.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.Adapter.FavoriteUserAdapter
import com.example.mysubmission3.R
import com.example.mysubmission3.databinding.ActivityFavoriteBinding
import com.example.mysubmission3.helper.UserFavoriteViewModelFactory
import com.example.mysubmission3.ui.ViewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteUserAdapter
    private var binding: ActivityFavoriteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = getString(R.string.favorite)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorite().observe(this) { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorite(favoriteList)
            }
        }

        adapter = FavoriteUserAdapter()
        binding?.recycleViewFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.recycleViewFavorite?.setHasFixedSize(true)
        binding?.recycleViewFavorite?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = UserFavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}