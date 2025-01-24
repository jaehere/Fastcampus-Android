package jaehee.study.news_app

data class NewsModel(
    val title: String,
    val link: String,
    var imageUrl: String? = null
)

fun List<NewsItem>.transform() : List<NewsModel> { // 타입 치환
    return this.map {
        NewsModel(
            title = it.title ?: "",
            link = it.link ?: "",
            imageUrl = null
        )
    }
}