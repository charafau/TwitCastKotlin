package com.nullpointerbay.twitcastkotlin.viewmodel

import com.nullpointerbay.twitcastkotlin.entity.AccessToken
import com.nullpointerbay.twitcastkotlin.model.AccessTokenModel
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import io.reactivex.Observable


class MainViewModel(val simpleKVStore: SimpleKVStore) {

    val accessTokenModel = AccessTokenModel(simpleKVStore)

    fun fetchAccessToken(code: String): Observable<AccessToken> {

        return accessTokenModel.fetchAccessToken(code)
                .doOnNext { (tokenType, expiresIn, accessToken) -> simpleKVStore.saveToken(accessToken) }
    }

    fun hasToken() = simpleKVStore.loadToken().isNotEmpty()


}