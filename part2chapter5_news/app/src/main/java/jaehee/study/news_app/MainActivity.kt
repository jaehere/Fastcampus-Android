package jaehee.study.news_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import jaehee.study.news_app.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://news.google.com/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )
        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter { url ->
            Log.e("url", url)
            startActivity(
                Intent(this, WebViewActivity::class.java).apply {
                    putExtra("url", url)
                }
            )
        }
        val newsService = retrofit.create(NewsService::class.java)

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        binding.feedChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.feedChip.isChecked = true
            newsService.mainFeed().submitList()
        }

        binding.politicsChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.politicsChip.isChecked = true

            newsService.politicsNews().submitList()
        }

        binding.economyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.economyChip.isChecked = true
            newsService.economyNews().submitList()
        }

        binding.societyChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.societyChip.isChecked = true
            newsService.societyNews().submitList()
        }

        binding.itChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.itChip.isChecked = true
            newsService.itNews().submitList()
        }

        binding.sportChip.setOnClickListener {
            binding.chipGroup.clearCheck()
            binding.sportChip.isChecked = true
            newsService.sportNews().submitList()
        }

        binding.searchTextInputEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.chipGroup.clearCheck()

                binding.searchTextInputEditText.clearFocus()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)

                newsService.search(binding.searchTextInputEditText.text.toString()).submitList()


                return@setOnEditorActionListener true //람다를 리턴하겠다.
            }
            return@setOnEditorActionListener false
        }

        binding.feedChip.isChecked = true
        newsService.mainFeed().submitList()

    }

    private fun Call<NewsRss>.submitList() { //확장함수
        this.enqueue(object : Callback<NewsRss> {
            override fun onResponse(p0: Call<NewsRss>, response: Response<NewsRss>) {
                Log.e("MainActivity", "${response.body()?.channel?.items}")

                val list = response.body()?.channel?.items.orEmpty().transform()
                newsAdapter.submitList(list)

                binding.notFoundView.isVisible = list.isEmpty()

                //Google News 에서 og:image 를 일괄로 Google News 로고로 변경하여 데이터가 잘 노출되지 않을 수 있음!
                //프로젝트이기 때문에 Google News 로고라도 잘 나오면 성공이지만, 실제 데이터가 나오는 것을 테스트 해보고 싶으시다면, Google News 의 RSS 피드 대신 다른 언론사의 RSS 피드로 테스트해보실 것을 권장드립니다.

                list.forEachIndexed { index, news ->
                    Thread { //네트워크 접속은 메인쓰레드에서 할 수 없기에, 별도로 쓰레드를 열어준다.
                        //쓰레드 시작은 보장하나, 끝나는 시점은 보장할 수 없다.
                        try {
                            val jsoup = Jsoup.connect(news.link).get()
                            val elements = jsoup.select("meta[property^=og:]")
                            val ogImageNode = elements.find { node ->
                                node.attr("property") == "og:image"
                            }
                            news.imageUrl = ogImageNode?.attr("content")

                            //Log.d("MainActivity","imageUrl: ${news.imageUrl}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        runOnUiThread {
                            newsAdapter.notifyItemChanged(index)
                        }
                    }.start()
                }


            }

            override fun onFailure(p0: Call<NewsRss>, p1: Throwable) {
                p1.printStackTrace()
            }

        })
    }
}