package com.nullpointerbay.twitcastkotlin.model

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nullpointerbay.twitcastkotlin.Urls
import com.nullpointerbay.twitcastkotlin.store.SimpleKVStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by charafau on 2017/01/27.
 */
interface Model {

    fun createRetrofit(simpleKvStore: SimpleKVStore): Retrofit {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(ApiHeaderInterceptor(simpleKvStore))
                .build()



        return Retrofit.Builder()
                .baseUrl(Urls.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

}

class ApiHeaderInterceptor(val simpleKVStore: SimpleKVStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val token = simpleKVStore.loadToken()
        if (token.isNotEmpty()) {
            var request: Request? = chain?.request()?.newBuilder()
                    ?.addHeader("X-Api-Version", "2.0")
                    ?.addHeader("Authorization", "Bearer $token")
//                    ?.addHeader("Accept-Encoding", "gzip")
                    ?.build()
            return chain?.proceed(request)!!
        } else {
            return chain?.proceed(chain?.request())!!
        }

    }


}