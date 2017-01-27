package com.nullpointerbay.twitcastkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_oauth.*
import java.net.URLDecoder

class OAuthActivity : AppCompatActivity() {

    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, OAuthActivity::class.java)
            activity.startActivityForResult(intent, OAUTH_REQUEST_CODE)
        }

        val OAUTH_REQUEST_CODE = 1001
        val CODE_RESULT = "code_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        web_oauth.setWebViewClient(OAuthWebViewClient(this))
        web_oauth.settings.javaScriptEnabled = true
        web_oauth.settings.javaScriptCanOpenWindowsAutomatically = true
        web_oauth.loadUrl(Urls.OAUTH_URL)

    }

    fun returnCodeAndFinish(code: String) {
        val result = Intent()
        result.putExtra(CODE_RESULT, code)
        setResult(Activity.RESULT_OK, result)

        finish()
    }
}


class OAuthWebViewClient(val oAuthActivity: OAuthActivity) : WebViewClient() {

    val TAG = "OAuthWebViewClient"

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

        Log.d(TAG, "got url now: $url")
        if (url?.startsWith("twitcastkotlin://oauth")!!) {
            val code = url.substring(url.indexOfFirst { it -> it == '=' } + 1)
            Log.d(TAG, "code: $code")
            oAuthActivity.returnCodeAndFinish(URLDecoder.decode(code, "UTF-8"))
        }

        return false
    }
}

