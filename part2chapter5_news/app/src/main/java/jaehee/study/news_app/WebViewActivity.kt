package jaehee.study.news_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import jaehee.study.news_app.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true

        if(url.isNullOrEmpty()) {
            Toast.makeText(this, "잘못된 URL 입니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            binding.webView.loadUrl(url)
        }

    }
}