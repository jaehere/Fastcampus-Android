package jaehee.p2ch1.webtoon

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import jaehee.p2ch1.webtoon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, WebViewFragment())
                commit() //transaction을 열었으면 commit을 해야한다.
            }
        }

        binding.button2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, BFragment())
                commit()
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.fragments[0]
                if (currentFragment is WebViewFragment) {
                    if (currentFragment.canGoBack()) {
                        currentFragment.goBack()
                    } else {
                        isEnabled = false // 기본 동작을 위해 콜백 비활성화
                        onBackPressedDispatcher.onBackPressed()

                    }
                } else {
                    isEnabled = false // 기본 동작을 위해 콜백 비활성화
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }

}