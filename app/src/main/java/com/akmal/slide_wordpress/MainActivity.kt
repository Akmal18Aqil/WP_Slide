package com.akmal.slide_wordpress
import retrofit2.*

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akmal.slide_wordpress.adapter.ArticleAdapter
import com.akmal.slide_wordpress.data.WordPressApi
import com.akmal.slide_wordpress.model.Article
import com.akmal.slide_wordpress.model.Category
import com.akmal.slide_wordpress.model.Media
import com.akmal.slide_wordpress.utils.HorizontalSpaceItemDecoration
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val articles = mutableListOf<Article>()
    private val categoriesMap = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_itemArticle)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(HorizontalSpaceItemDecoration(10))

        val retrofit = Retrofit.Builder()
            .baseUrl("https://annur2.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WordPressApi::class.java)

        api.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                response.body()?.forEach { category ->
                    categoriesMap[category.id] = category.name
                }
                fetchArticles(api)
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun fetchArticles(api: WordPressApi) {
        api.getArticles(10).enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                val articlesResponse = response.body() ?: return

                articlesResponse.forEach { article ->
                    api.getMedia(article.featured_media).enqueue(object : Callback<Media> {
                        override fun onResponse(call: Call<Media>, response: Response<Media>) {
                            ArticleAdapter.mediaMap[article.featured_media] = response.body()?.source_url ?: ""
                            articles.add(article)
                            if (articles.size == articlesResponse.size) {
                                recyclerView.adapter = ArticleAdapter(articles, categoriesMap)
                            }
                        }

                        override fun onFailure(call: Call<Media>, t: Throwable) {
                            // Handle error
                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                // Handle error
            }
        })
    }
}