package com.akmal.slide_wordpress.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akmal.slide_wordpress.model.Article
import com.akmal.slide_wordpress.R
import com.bumptech.glide.Glide

class ArticleAdapter(private val articles: List<Article>, private val category: Map<Int, String>): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ArticleImage: ImageView = view.findViewById(R.id.iv_image)
        val ArticleTitle: TextView = view.findViewById(R.id.tv_title)
        val ArticleCategory: TextView = view.findViewById(R.id.tv_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.ArticleTitle.text = article.title.rendered
        holder.ArticleCategory.text = category[article.categories.firstOrNull() ?:0]?:"Uncategorized"

        Glide.with(holder.itemView.context)
            .load(mediaMap[article.featured_media])
            .into(holder.ArticleImage)
    }
    companion object {
        val mediaMap = mutableMapOf<Int, String>()
    }
}