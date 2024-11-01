package jaehee.p2ch1.webtoon

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import jaehee.p2ch1.webtoon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnTabLayoutNameChanged {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences =
            getSharedPreferences(WebViewFragment.Companion.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val tab0 = sharedPreferences?.getString("tab0_name", "월요웹툰")
        val tab1 = sharedPreferences?.getString("tab1_name", "화요웹툰")
        val tab2 = sharedPreferences?.getString("tab2_name", "수요웹툰")




        binding.viewPager.adapter = ViewPagerAdapter(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            run {
                tab.text = when (position) {
                    0 -> tab0
                    1 -> tab1
                    else -> tab2
                }
            }

        }.attach()

    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment =
                    supportFragmentManager.fragments[binding.viewPager.currentItem]
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

    override fun nameChanged(position: Int, name: String) {
        val tab = binding.tabLayout.getTabAt(position)
        tab?.text = name
    }

}