package ku.olga.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_poi.view.*
import ku.olga.core_api.dto.POI
import ku.olga.ui_core.base.BaseAdapter
import ku.olga.ui_core.utils.highlight

class POIAdapter : BaseAdapter<POI, POIAdapter.POIHolder>() {
    private var query: String = ""
    var onClickListener: ((POI) -> (Unit))? = null
    var highlightColor: Int = Color.BLACK

    fun setQuery(query: String?) {
        if (this.query != query) {
            this.query = query ?: ""
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): POIHolder =
        POIHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poi, parent, false))

    override fun onBindViewHolder(holder: POIHolder, position: Int) {
        holder.bindPOI(getItem(position))
    }

    inner class POIHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var poi: POI? = null

        init {
            itemView.setOnClickListener { poi?.let { onClickListener?.invoke(it) } }
        }

        fun bindPOI(poi: POI?) {
            this.poi = poi
            itemView.textViewTitle.text = highlight(poi?.title, query, highlightColor)
        }
    }
}