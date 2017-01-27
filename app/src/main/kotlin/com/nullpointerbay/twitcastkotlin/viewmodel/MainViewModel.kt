package com.nullpointerbay.twitcastkotlin.viewmodel

import com.nullpointerbay.twitcastkotlin.entity.AccessToken
import com.nullpointerbay.twitcastkotlin.model.AccessTokenModel
import com.nullpointerbay.twitcastkotlin.model.MovieModel
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import io.reactivex.Observable


class MainViewModel(val simpleKVStore: SimpleKVStore) {

    val accessTokenModel = AccessTokenModel(simpleKVStore)

    val movieModel = MovieModel(simpleKVStore)

    fun fetchAccessToken(code: String): Observable<AccessToken> = accessTokenModel.fetchAccessToken(code)
            .doOnNext { (tokenType, expiresIn, accessToken) -> simpleKVStore.saveToken(accessToken) }

    fun fetchRecommendedMovies() = movieModel.searchRecommendedMovies()

    fun hasToken() = simpleKVStore.loadToken().isNotEmpty()


}