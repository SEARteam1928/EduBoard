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
import android.widget.*
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
    private var levelTv: TextView? = null

    private lateinit var mapImage: ImageView

    private var selectInt: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            initViews()
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btSend -> openBrowser("http://192.168.10.1/config?id=$selectInt$selectInt$selectInt,$selectInt$selectInt$selectInt,$selectInt$selectInt$selectInt,$selectInt$selectInt$selectInt,$selectInt$selectInt$selectInt")
            R.id.btNext -> toNextImage()
            R.id.btPrev -> toPrevImage()
        }
        select()
        levelTv!!.text = "Уровень " + (selectInt).toString()
    }

    private fun toNextImage() {
        if (selectInt < 7) {
            btPrev!!.visibility = View.VISIBLE
            selectInt++
        } else {
            btNext!!.visibility = View.GONE
        }
    }

    private fun toPrevImage() {
        if (selectInt > 1) {
            btNext!!.visibility = View.VISIBLE
            selectInt--
        } else {
            btPrev!!.visibility = View.GONE
        }
    }

    private fun setImg(id: Int){
        mapImage.setImageDrawable(resources.getDrawable(id))
    }

    private fun select() {
        when (selectInt) {
            1 -> setImg(R.drawable.field_1)
            2 -> setImg(R.drawable.field_2)
            3 -> setImg(R.drawable.field_3)
            4 -> setImg(R.drawable.field_4)
            5 -> setImg(R.drawable.field_5)
            6 -> setImg(R.drawable.field_6)
            7 -> setImg(R.drawable.field_7)
        }
    }

    private fun openBrowser(url: String) {
        webChanges!!.loadUrl(url)
        Toast.makeText(this@MainActivity, url, Toast.LENGTH_LONG).show()
        select()
    }

    private fun initViews() {
        webChanges = findViewById(R.id.webChanges)
        btSend = findViewById(R.id.btSend)
        btSend!!.setOnClickListener(this)

        btNext = findViewById(R.id.btNext)
        btNext!!.setOnClickListener(this)

        btPrev = findViewById(R.id.btPrev)
        btPrev!!.setOnClickListener(this)

        mapImage = findViewById(R.id.mapImage)
        levelTv = findViewById(R.id.levelTv)
        levelTv!!.text = "Уровень 1"
        mapImage.setImageDrawable(resources.getDrawable(R.drawable.field_1))

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

            val conManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return (conManager.activeNetworkInfo != null
                    && conManager.activeNetworkInfo!!.isAvailable
                    && conManager.activeNetworkInfo!!.isConnected)
        }
    }
}
