package me.zeroest.part2_ch08

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val addressBar: EditText by lazy {
        findViewById(R.id.addressBar)
    }

    private val homeButton: ImageButton by lazy {
        findViewById(R.id.homeButton)
    }
    private val forwardButton: ImageButton by lazy {
        findViewById(R.id.forwardButton)
    }
    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        bindViews()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val url = textView.text.toString()
                if (URLUtil.isNetworkUrl(url)) {
                    webView.loadUrl(url)
                } else {
                    webView.loadUrl("http://$url")
                }
            }

            return@setOnEditorActionListener false
        }

        backButton.setOnClickListener {
            webView.goBack()
        }

        forwardButton.setOnClickListener {
            webView.goForward()
        }

        homeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    inner class WebViewClient: android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }



        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.hide()

            backButton.isEnabled = webView.canGoBack()
            forwardButton.isEnabled = webView.canGoForward()

            addressBar.setText(url)
        }

    }

    inner class WebChromeClient: android.webkit.WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress
        }
    }

}