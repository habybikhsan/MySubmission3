package com.example.mysubmission3.ui.activity

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
import com.example.mysubmission3.BuildConfig
import com.example.mysubmission3.db.Data.User
import com.example.mysubmission3.db.Data.UserDetail
import com.example.mysubmission3.db.UserFavorite
import com.example.mysubmission3.R
import com.example.mysubmission3.ui.ViewModel.MenuViewModel
import com.example.mysubmission3.ui.ViewModel.SettingPreferences
import com.example.mysubmission3.helper.ViewModelFactory
import com.example.mysubmission3.databinding.ActivityUserDetailBinding
import com.example.mysubmission3.helper.UserFavoriteViewModelFactory
import com.example.mysubmission3.ui.ViewModel.FavoriteAddViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var listUserDetail = ArrayList<UserDetail>()
    private lateinit var favoriteAddViewModel: FavoriteAddViewModel
    private var binding: ActivityUserDetailBinding? = null
    private lateinit var imageAvatar: String
    private var userFavorite: UserFavorite? = null
    private var isliked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        favoriteAddViewModel = obtainViewModel(this@UserDetailActivity)

        val user = intent.getParcelableExtra<User>(EXTRA_DATA) as User

        userFavorite = intent.getParcelableExtra(EXTRA_NOTE)
        if (userFavorite!=null){
            getUserDetail(userFavorite!!.username.toString())
            isliked = true
            setFavoriteStatus(isliked)
        }else
            getUserDetail(user.username.toString())

        viewPagerConfig()
        getThemeSetting()

        binding?.btnFavorite?.setOnClickListener(this)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddViewModel {
        val factory = UserFavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteAddViewModel::class.java]
    }

    private fun getUserDetail(id: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "Api_Key $Api_Key")
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
        binding?.let {
            Glide.with(this@UserDetailActivity)
                .load(userdetail.avatar)
                .into(it.detailPhoto)
        }
        imageAvatar = userdetail.avatar.toString()
        userdetail.username?.let { setActionBarTitle(it) }
        if (userdetail.name?.isEmpty() == true || userdetail.name == "null")
            binding?.detailName2?.text = getString(R.string.no_data)
        else binding?.detailName2?.text = userdetail.name

        binding?.detailUsername?.text = userdetail.username

        if (userdetail.location?.isEmpty() == true || userdetail.location == "null")
            binding?.detailLocation?.text = getString(R.string.location, getString(R.string.no_data))
        else binding?.detailLocation?.text = getString(R.string.location, userdetail.location)

        if (userdetail.company?.isEmpty() == true || userdetail.company == "null")
            binding?.detailCompany?.text = getString(R.string.company,getString(R.string.no_data))
        else binding?.detailCompany?.text = getString(R.string.company, userdetail.company )

        binding?.detailRepository?.text = getString(R.string.repository, userdetail.repository.toString())
        binding?.detailFollowers?.text = getString(R.string.followers, userdetail.followers.toString())
        binding?.detailFollowing?.text = getString(R.string.following, userdetail.following.toString())
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding!!.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding!!.tabs
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
        if (isLoading) binding?.progressBar2?.visibility = View.VISIBLE
        else binding?.progressBar2?.visibility = View.GONE
    }

    private fun setFavoriteStatus(state: Boolean) {
        if (state) binding?.btnFavorite?.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        else binding?.btnFavorite?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_favorite ->{
                if (!isliked){
                    addUserFavorite()
                    isliked = !isliked
                    setFavoriteStatus(isliked)
                }else{
                    deleteUserFavorite()
                    setFavoriteStatus(!isliked)
                    isliked = false
                }

            }
        }
    }

    private fun deleteUserFavorite() {
        favoriteAddViewModel.delete(userFavorite as UserFavorite)
        showToast(getString(R.string.label_deleted_favorite))
    }

    private fun addUserFavorite() {
        val username = binding?.detailUsername?.text.toString()
        val avatar = imageAvatar
        val location = binding?.detailLocation?.text.toString()
        val comapany = binding?.detailCompany?.text.toString()

        userFavorite.let { userFavorite ->
            userFavorite?.username = username
            userFavorite?.avatar = avatar
            userFavorite?.location = location
            userFavorite?.company = comapany
        }

        favoriteAddViewModel.insert(userFavorite as UserFavorite)
        showToast(getString(R.string.label_add_favorite))

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object{
        var TAG: String = UserDetailActivity::class.java.simpleName
        var EXTRA_DATA = "0"
        const val EXTRA_NOTE = "extra_note"
        private const val Api_Key = BuildConfig.TOKEN
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}