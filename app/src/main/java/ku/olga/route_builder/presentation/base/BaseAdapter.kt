package ku.olga.route_builder.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val items = mutableListOf<T>()

    fun clear() {
        items.clear()
    }

    fun setItems(items: List<T>?) {
        this.items.clear()
        items?.let { this.items.addAll(it) }
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = items[position]

    fun addItem(position: Int, item: T) {
        if (items.size >= position) {
            items.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && items.size > position) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = items.size
}