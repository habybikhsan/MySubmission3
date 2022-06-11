package com.example.mysubmission3.Activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.Data.User
import com.example.mysubmission3.ViewModel.MenuViewModel
import com.example.mysubmission3.R
import com.example.mysubmission3.ViewModel.SettingPreferences
import com.example.mysubmission3.ViewModel.ViewModelFactory
import com.example.mysubmission3.Adapter.ListUserAdapter
import com.example.mysubmission3.BuildConfig
import com.example.mysubmission3.databinding.ActivityDarkModeBinding
import com.example.mysubmission3.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSetting : ActivityDarkModeBinding
    private var listData = ArrayList<User>()
    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.title_app)
        adapter = ListUserAdapter(listData)
        getUser()
        getThemeSetting()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    listData.clear()
                    getUserSearch(query)
                }
                return true
            }

            override fun onQueryTextSubmit(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.SettingTheme ->{
                startActivity(Intent(this, DarkModeActivity::class.java))
                true
            }
            R.id.Favorite ->{
                startActivity(Intent(this, FavoriteActivity::class.java))
                true
            }
            else -> true
        }
    }
    private fun getUserSearch(id: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                showLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val avatar: String = jsonObject.getString("avatar_url").toString()
                        val username: String = jsonObject.getString("login")
                        val id: String = jsonObject.getString("id")
                        val type: String = jsonObject.getString("type")
                        listData.add(
                            User(
                            username,
                            avatar,
                            id,
                            type)
                        )
                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getUser() {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                showLoading(false)
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val avatar: String = jsonObject.getString("avatar_url").toString()
                        val username: String = jsonObject.getString("login")
                        val id: String = jsonObject.getString("id")
                        val type: String = jsonObject.getString("type")
                        listData.add(
                            User(
                            username,
                            avatar,
                            id,
                            type)
                        )
                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    private fun showRecyclerList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
        binding.rvUser.adapter = adapter
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_DATA, data)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    private fun getThemeSetting() {
        bindingSetting = ActivityDarkModeBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(datastore)
        val menuViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MenuViewModel::class.java]

        menuViewModel.getThemeSetting().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                bindingSetting.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                bindingSetting.switchTheme.isChecked = false
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
        val token = BuildConfig.TOKEN
    }
}
