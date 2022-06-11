package com.example.mysubmission3.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission3.Activity.UserDetailActivity
import com.example.mysubmission3.BuildConfig
import com.example.mysubmission3.Data.UserDetail
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@Suppress("OPT_IN_IS_NOT_ENABLED")
class DetailUserViewModel : ViewModel() {
    companion object {
        private val TAG = DetailUserViewModel::class.java.simpleName
        private const val API_KEY = BuildConfig.TOKEN
    }
    private val detaiUser = MutableLiveData<UserDetail>()

    fun setDetailUser(id: String, context: Context) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $API_KEY")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(UserDetailActivity.TAG, result)
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
                    detaiUser.postValue(UserDetail())

                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(TAG, errorMessage)
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    fun getDetailUser(): LiveData<UserDetail> = detaiUser
}