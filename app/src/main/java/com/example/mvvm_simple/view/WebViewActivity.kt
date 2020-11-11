package com.example.mvvm_simple.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lib_common.statusbar.StatusBarUtil
import com.example.lib_common.statusbar.SystemBarTintManager
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivityWebviewBinding
import com.google.android.material.appbar.MaterialToolbar
import io.netty.handler.codec.http.HttpResponseStatus

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WebViewActivity: AppCompatActivity() {

    private var mWebView: WebView? = null
    private lateinit var mDataBinding: ActivityWebviewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        StatusBarUtil.setTranslucentStatus(this)
        initWebView()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            toolbar.title = "近享"
            setActionBar(toolbar)
        }

        if (intent != null) {
            val data: String = intent.getStringExtra("url")
            mWebView?.loadUrl(data)
        }
    }

    /**
     * 设置WebView的参数
     */
    private fun initWebView() {
        mWebView = findViewById(R.id.webview)
        val webSettings = mWebView?.settings
        webSettings?.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        webSettings?.useWideViewPort = true; //将图片调整到适合webview的大小
        webSettings?.loadWithOverviewMode = true; // 缩放至屏幕的大小
        //缩放操作
        webSettings?.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings?.builtInZoomControls = true; //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings?.displayZoomControls = false; //隐藏原生的缩放控件
        webSettings?.javaScriptCanOpenWindowsAutomatically = true; //支持通过JS打开新窗口
        webSettings?.loadsImagesAutomatically = true; //支持自动加载图片
        webSettings?.defaultTextEncodingName = "utf-8";//设置编码格式

        //对网络请求进行处理,使得打开网页时不调用系统浏览器,而是在本WebView中显示
        mWebView?.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }



            //加载失败时调用，如404
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                when(errorCode) {
                    HttpResponseStatus.NOT_FOUND.code() -> view?.loadUrl("https://www.baidu.com/")
                    else -> view?.loadUrl("https://www.baidu.com/")
                }
            }

            //webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }


        }

//        mWebView?.webChromeClient = object : WebChromeClient() {
//
//            /**
//             * 在这里可以获得网页的加载进度
//             */
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                if (newProgress < 100) {
//                    mDataBinding.progressBar.visibility = View.VISIBLE;
//                } else {
//                    mDataBinding.progressBar.visibility = View.GONE;
//                }
//            }
//
//            /**
//             * 在这里可以获得网页的标题
//             */
//            override fun onReceivedTitle(view: WebView?, title: String?) {
//                if (actionBar != null) {
//                    actionBar.title = title
//                }
//            }
//
//        }
    }

    /**
     * Back键控制网页后退
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KEYCODE_BACK) && mWebView?.canGoBack()!!) {
            mWebView?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
        mWebView?.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
        mWebView?.resumeTimers()
    }

    override fun onDestroy() {
        //防止内存泄漏，先移除webView，再销毁WebView,最后置空
        mWebView?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        mWebView?.clearHistory();
        (mWebView?.parent as ViewGroup).removeView(mWebView)
        mWebView?.destroy();
        mWebView = null
        super.onDestroy()
    }

    companion object {
        //用于外界跳转界面
        fun startAction(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }
}