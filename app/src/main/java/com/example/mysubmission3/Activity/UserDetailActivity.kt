package com.example.mysubmission3.Activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mysubmission3.Adapter.SectionPagerAdapter
import com.example.mysubmission3.Data.User
import com.example.mysubmission3.Data.UserDetail
import com.example.mysubmission3.Data.UserFavorite
import com.example.mysubmission3.R
import com.example.mysubmission3.ViewModel.MenuViewModel
import com.example.mysubmission3.ViewModel.SettingPreferences
import com.example.mysubmission3.ViewModel.ViewModelFactory
import com.example.mysubmission3.databinding.ActivityUserDetailBinding
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.AVATAR
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.COMPANY
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.CONTENT_URI
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.FAVORITE
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.FOLLOWERS
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.FOLLOWING
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.NAME
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.REPOSITORY
import com.example.mysubmission3.db.DatabaseContractFavorite.FavoriteColumns.Companion.USERNAME
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var listUserDetail = ArrayList<UserDetail>()
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var favoriteUserGitHelper: FavoriteUserGitHelper
    private lateinit var imageAvatar: String
    private var favorites: UserFavorite? = null
    private var liked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_DATA) as User

        favoriteUserGitHelper = FavoriteUserGitHelper.getInstance(applicationContext)
        favoriteUserGitHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorites!=null){
            getUserDetail(favorites!!.username.toString())
            liked = true
            setFavoriteStatus(liked)
        }else
            getUserDetail(user.username.toString())

        viewPagerConfig()
        getThemeSetting()

        binding.btnFavorite.setOnClickListener(this)
    }

    private fun getUserDetail(id: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id"
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
                    val jsonObject = JSONObject(result)
                    val username = jsonObject.getString("login").toString()
                    val name = jsonObject.getString("name").toString()
                    val avatar: String = jsonObject.getString("avatar_url").toString()
                    val company: String = jsonObject.getString("company").toString()
                    val location: String = jsonObject.getString("location").toString()
                    val repository: Int = jsonObject.getInt("public_repos")
                    val followers: Int = jsonObject.getInt("followers")
                    val following: Int = jsonObject.getInt("following")
                    val userdetail = UserDetail(
                        username,
                        avatar,
                        name,
                        company,
                        location,
                        repository,
                        followers,
                        following
                    )
                    listUserDetail.add(userdetail)
                    showUserDetail(userdetail)

                } catch (e: Exception) {
                    Toast.makeText(this@UserDetailActivity, e.message, Toast.LENGTH_SHORT)
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
                Toast.makeText(this@UserDetailActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showUserDetail(userdetail: UserDetail){
        Glide.with(this@UserDetailActivity)
            .load(userdetail.avatar)
            .into(binding.detailPhoto)
        imageAvatar = userdetail.avatar.toString()
        userdetail.username?.let { setActionBarTitle(it) }
        if (userdetail.name?.isEmpty() == true || userdetail.name == "null")
            binding.detailName2.text = getString(R.string.no_data)
        else binding.detailName2.text = userdetail.name

        binding.detailUsername.text = userdetail.username

        if (userdetail.location?.isEmpty() == true || userdetail.location == "null")
            binding.detailLocation.text = getString(R.string.location, getString(R.string.no_data))
        else binding.detailLocation.text = getString(R.string.location, userdetail.location)

        if (userdetail.company?.isEmpty() == true || userdetail.company == "null")
            binding.detailCompany.text = getString(R.string.company,getString(R.string.no_data))
        else binding.detailCompany.text = getString(R.string.company, userdetail.company )

        binding.detailRepository.text = getString(R.string.repository, userdetail.repository.toString())
        binding.detailFollowers.text = getString(R.string.followers, userdetail.followers.toString())
        binding.detailFollowing.text = getString(R.string.following, userdetail.following.toString())
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }
    }

    private fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(datastore)
        ViewModelProvider(this, ViewModelFactory(pref))[MenuViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar2.visibility = View.VISIBLE
        else binding.progressBar2.visibility = View.GONE
    }

    private fun setFavoriteStatus(state: Boolean) {
        if (state) binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        else binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }
    companion object{
        var EXTRA_POSITION = "0"
        var TAG: String = UserDetailActivity::class.java.simpleName
        var EXTRA_DATA = "0"
        const val EXTRA_NOTE = "extra_note"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_favorite) {
            if (liked) {
               favoriteUserGitHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.label_deleted_favorite), Toast.LENGTH_SHORT)
                    .show()
                liked = false
                setFavoriteStatus(liked)
            } else {
                val value = ContentValues()
                value.put(USERNAME, binding.detailUsername.text.toString())
                value.put(NAME, binding.detailName2.text.toString())
                value.put(AVATAR, imageAvatar)
                value.put(COMPANY, binding.detailCompany.text.toString())
                val strdataRepository = binding.detailRepository.text.toString()
                val dataRepository = strdataRepository.split(" ").toTypedArray()
                value.put(REPOSITORY, dataRepository[2])
                val strdataFollowers = binding.detailFollowers.text.toString()
                val dataFollowers = strdataFollowers.split(" ").toTypedArray()
                value.put(FOLLOWERS, dataFollowers[2])
                val strdataFollowing = binding.detailFollowing.text.toString()
                val dataFollowing = strdataFollowing.split(" ").toTypedArray()
                value.put(FOLLOWING, dataFollowing[2])
                value.put(FAVORITE, "1")

                liked = true
                contentResolver.insert(CONTENT_URI, value)
                Toast.makeText(this, getString(R.string.label_add_favorite), Toast.LENGTH_SHORT)
                    .show()
                setFavoriteStatus(liked)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteUserGitHelper.close()
    }
}