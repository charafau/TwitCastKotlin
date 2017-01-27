package com.nullpointerbay.twitcastkotlin.model

import com.nullpointerbay.twitcastkotlin.entity.ApiSearchResult
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

class MovieModel(simpleKVStore: SimpleKVStore) : Model {

    val movieApi = createRetrofit(simpleKVStore).create(MovieApi::class.java)

    fun searchRecommendedMovies(limit: Int = 10) = movieApi.getSearchLives(limit)
            .map { it -> it.movies }

}


interface MovieApi {

    @GET("search/lives")
    fun getSearchLives(@Query("limit") limit: Int = 10,
                       @Query("type") type: String = "new",
                       @Query("context") context: String = "",
                       @Query("lang") language: String = "ja")
            : Observable<ApiSearchResult>

}

sealed class SearchMovieType(val type: String) {
    object SearchTag : SearchMovieType("tag")
    object SearchWord : SearchMovieType("word")
    object SearchCategory : SearchMovieType("category")
    object SearchNew : SearchMovieType("new")
    object SearchRecommend : SearchMovieType("recommend")
}