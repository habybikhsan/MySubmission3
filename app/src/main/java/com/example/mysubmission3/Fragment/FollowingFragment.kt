package com.example.mysubmission3.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.Data.User
import com.example.mysubmission3.R
import com.example.mysubmission3.Adapter.FollowingAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray


class FollowingFragment : Fragment() {

    private var listData: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter(listData)
        val dataUser = activity?.intent?.getParcelableExtra<User>(EXTRA_DATA) as User
        getuserfollowing(dataUser.username.toString())

    }

    private fun getuserfollowing(id: String) {
        progressBarFollowing.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBarFollowing.visibility = View.INVISIBLE
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
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
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
                progressBarFollowing.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
    private fun showRecyclerList() {
        recycleViewFollowing.layoutManager = LinearLayoutManager(activity)
        recycleViewFollowing.setHasFixedSize(true)
        recycleViewFollowing.adapter = adapter
    }
    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_DATA = "0"
    }
}