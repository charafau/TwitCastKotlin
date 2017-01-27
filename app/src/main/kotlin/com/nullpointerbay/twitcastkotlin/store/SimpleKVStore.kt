package com.nullpointerbay.twitcastkotlin.store

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SimpleKVStore(val context: Context) {

    companion object {
        val SHARED_PREFERENCES_NAME = "app_shared"
        val PREF_TOKEN = "pref_token"
        val TAG = "SimpleKVStore"
    }

    val sp: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {

        Log.d(TAG, "saving token: $token")
        sp.edit().putString(PREF_TOKEN, token).apply()
    }

    fun loadToken(): String = sp.getString(PREF_TOKEN, "")


}