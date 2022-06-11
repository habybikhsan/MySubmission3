package com.example.mysubmission3.Activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.Adapter.FavoriteUserAdapter
import com.example.mysubmission3.Data.UserFavorite
import com.example.mysubmission3.R
import com.example.mysubmission3.databinding.ActivityFavoriteBinding
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.CONTENT_URI
import com.example.mysubmission3.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite)

        binding.recycleViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recycleViewFavorite.setHasFixedSize(true)
        adapter = FavoriteUserAdapter(this)
        binding.recycleViewFavorite.adapter = adapter

        loadFavoriteAsync()
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null)
                adapter.listFavoriteUser = list
        }
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            statusLoading(true)
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            statusLoading(false)
            if (favData.size > 0) {
                adapter.listFavoriteUser = favData
            } else {
                adapter.listFavoriteUser = ArrayList()
                showSnackbarMessage()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUser)
    }

    private fun showSnackbarMessage() {
        Toast.makeText(this, getString(R.string.label_empty_favorite), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

    private fun statusLoading(status: Boolean) {
        if (status) {
            progressBarFavorite.visibility = View.VISIBLE
        } else {
            progressBarFavorite.visibility = View.INVISIBLE
        }
    }
}