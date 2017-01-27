package com.nullpointerbay.twitcastkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nullpointerbay.twitcastkotlin.entity.SearchResult
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import com.nullpointerbay.twitcastkotlin.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_movie.view.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val mainViewModel: MainViewModel by lazy {
        MainViewModel(SimpleKVStore(this))
    }

    private val moviesAdapter = MoviesAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_movies.layoutManager = LinearLayoutManager(this)
        recycler_movies.setHasFixedSize(true)
        recycler_movies.adapter = moviesAdapter


    }

    override fun onResume() {
        super.onResume()
        if (!mainViewModel.hasToken()) {
            OAuthActivity.start(this)
        } else {
            mainViewModel.fetchRecommendedMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { it ->
                                Log.d(TAG, "items: $it")
                                moviesAdapter.addAll(it)
                                moviesAdapter.notifyDataSetChanged()
                            },
                            { e -> Toast.makeText(this@MainActivity, "Error fetching movies: ${e.message}", Toast.LENGTH_LONG)
                                Log.e(TAG, "error: ${e.message}")}
                    )
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

class MoviesAdapter(var items: List<SearchResult>) : RecyclerView.Adapter<MovieViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MovieViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    fun addAll(movies: List<SearchResult>?) {
        items = movies!!
    }

}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(searchResult: SearchResult) = with(itemView) {
        txt_movie.text = searchResult.movie.title
    }

}
