package com.example.eduboard

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var webChanges: WebView? = null
    private var btSend: Button? = null
    private var btNext: Button? = null
    private var btPrev: Button? = null

    private lateinit var mapImage: ImageView

    private var selectInt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            initViews()
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btSend -> openBrowser("http://192.168.4.1/?id="+"Send")
            R.id.btNext -> openBrowser("http://192.168.4.1/?id="+"Next")
            R.id.btPrev -> openBrowser("http://192.168.4.1/?id="+"Prev")
        }
    }

    private fun select(){
        when(selectInt){
            0 ->{
                selectInt = 1
                mapImage.visibility = View.GONE
            }
            1 ->{
                selectInt = 0
                mapImage.visibility = View.VISIBLE
            }
        }
    }

    private fun openBrowser(url: String){
        webChanges!!.loadUrl(url)
        Toast.makeText(this@MainActivity,url,Toast.LENGTH_LONG).show()
        select()
    }

    private fun initViews(){
        webChanges = findViewById(R.id.webChanges)
        btSend = findViewById(R.id.btSend)
        btSend!!.setOnClickListener(this)

        btNext = findViewById(R.id.btNext)
        btNext!!.setOnClickListener(this)

        btPrev = findViewById(R.id.btPrev)
        btPrev!!.setOnClickListener(this)

        mapImage = findViewById(R.id.mapImage)

        webChanges!!.settings.javaScriptEnabled
        webChanges!!.settings.builtInZoomControls
        webChanges!!.settings.supportZoom()
        webChanges!!.settings.displayZoomControls
        webChanges!!.settings.loadWithOverviewMode
        webChanges!!.settings.defaultFixedFontSize = 15
        webChanges!!.settings.setAppCacheMaxSize(20 * 1024 * 1024)
        webChanges!!.settings.setAppCachePath(this.cacheDir.absolutePath)
        webChanges!!.settings.allowFileAccess
        webChanges!!.settings.setAppCacheEnabled(true)
        webChanges!!.settings.cacheMode = WebSettings.LOAD_DEFAULT

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(this, "Отсутствует подключение!", Toast.LENGTH_SHORT).show()
            webChanges!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
    }

    object DetectConnection {
        fun checkInternetConnection(context: Context?): Boolean {

            val conManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return (conManager.activeNetworkInfo != null
                    && conManager.activeNetworkInfo!!.isAvailable
                    && conManager.activeNetworkInfo!!.isConnected)
        }
    }
}
