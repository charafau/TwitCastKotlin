package com.nullpointerbay.twitcastkotlin.model

import com.nullpointerbay.twitcastkotlin.ConfigKeys
import com.nullpointerbay.twitcastkotlin.entity.AccessToken
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class AccessTokenModel(simpleKVStore: SimpleKVStore) : Model {


    val accessTokenApi = createRetrofit(simpleKVStore).create(AccessTokenApi::class.java)

    fun fetchAccessToken(code: String): Observable<AccessToken>
            = accessTokenApi.getAccessToken(code, ConfigKeys.CLIENT_ID, ConfigKeys.CLIENT_SECRET)


}

interface AccessTokenApi {

    @FormUrlEncoded
    @POST("oauth2/access_token")
    fun getAccessToken(@Field("code") code: String,
                       @Field("client_id") clientId: String,
                       @Field("client_secret") clientSecret: String,
                       @Field("grant_type") grantType: String = "authorization_code",
                       @Field("redirect_uri") redirectUri: String = "twitcastkotlin://oauth")
            : Observable<AccessToken>
}