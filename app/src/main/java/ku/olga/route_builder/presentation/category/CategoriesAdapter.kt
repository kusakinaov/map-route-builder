package ku.olga.route_builder.presentation.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.presentation.base.BaseAdapter

class CategoriesAdapter : BaseAdapter<Category, CategoriesAdapter.CategoryHolder>() {
    private var query: String = ""
    var categoryClickListener: ((Category) -> (Unit))? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CategoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setQuery(query: String?) {
        this.query = query ?: ""
    }

    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var category: Category? = null

        init {
            itemView.setOnClickListener { category?.let { categoryClickListener?.invoke(it) } }
        }

        fun bind(category: Category?) {
            this.category = category
            itemView.textViewTitle.text = category?.title
        }
    }
}