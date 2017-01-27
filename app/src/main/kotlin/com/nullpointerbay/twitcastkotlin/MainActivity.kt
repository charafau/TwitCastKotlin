package com.nullpointerbay.twitcastkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.nullpointerbay.twitcastkotlin.entity.Movie
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import com.nullpointerbay.twitcastkotlin.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val mainViewModel: MainViewModel by lazy {
        MainViewModel(SimpleKVStore(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mainViewModel.hasToken()) {
            recycler_movies.layoutManager = LinearLayoutManager(this)
            recycler_movies.setHasFixedSize(true)
//            recycler_movies.adapter = MoviesAdapter()
        }

    }

    override fun onResume() {
        super.onResume()
        if (!mainViewModel.hasToken()) {
            OAuthActivity.start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "result..")

        if (requestCode == OAuthActivity.OAUTH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "should have token ${data?.getStringExtra(OAuthActivity.CODE_RESULT)}")
            data?.getStringExtra(OAuthActivity.CODE_RESULT)?.let {
                mainViewModel.fetchAccessToken(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { bool -> Log.d(TAG, "gowno $bool") },
                                { e -> Log.d(TAG, "err : ${e.message}") }
                        )
            }
        }

    }
}

class MoviesAdapter(items: List<Movie>) : RecyclerView.Adapter<MovieViewHolder>() {
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: MovieViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MovieViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class MovieViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

}
