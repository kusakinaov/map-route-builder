package ku.olga.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import ku.olga.core_api.dto.Category
import ku.olga.ui_core.base.BaseAdapter
import ku.olga.ui_core.utils.highlight
import javax.inject.Inject

class CategoriesAdapter @Inject constructor() :
    BaseAdapter<Category, CategoriesAdapter.CategoryHolder>() {
    private var query: String = ""
    var categoryClickListener: ((Category) -> (Unit))? = null
    var highlightColor: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setQuery(query: String?) {
        if (this.query != query) {
            this.query = query ?: ""
            notifyDataSetChanged()
        }
    }

    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var category: Category? = null

        init {
            itemView.setOnClickListener { category?.let { categoryClickListener?.invoke(it) } }
        }

        fun bind(category: Category?) {
            this.category = category

            itemView.textViewTitle.text = highlight(category?.title, query, highlightColor)
        }
    }
}