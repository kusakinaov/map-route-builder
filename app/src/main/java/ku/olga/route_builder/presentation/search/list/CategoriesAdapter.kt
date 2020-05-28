package ku.olga.route_builder.presentation.search.list

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import ku.olga.route_builder.R
import ku.olga.core_api.dto.Category
import ku.olga.ui_core.BaseAdapter
import java.util.*
import javax.inject.Inject

class CategoriesAdapter @Inject constructor() : BaseAdapter<Category, CategoriesAdapter.CategoryHolder>() {
   private var query: String = ""
   var categoryClickListener: ((Category) -> (Unit))? = null

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
           CategoryHolder(
                   LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
           )

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

         val title = category?.title ?: ""
         itemView.textViewTitle.text = SpannableStringBuilder(title).apply {
            val startIndex = title.toLowerCase(Locale.ROOT).indexOf(query.toLowerCase(Locale.ROOT))
            if (startIndex >= 0)
               setSpan(
                       ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.secondaryColor)),
                       startIndex, startIndex + query.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
               )
         }
      }
   }
}