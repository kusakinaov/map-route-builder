package ku.olga.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_poi.view.*
import ku.olga.core_api.dto.POI
import ku.olga.ui_core.base.BaseAdapter

class POIAdapter : BaseAdapter<POI, POIAdapter.POIHolder>() {

    class POIHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindPOI(poi: POI?) {
            itemView.textViewTitle.text = poi?.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): POIHolder =
        POIHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poi, parent, false))

    override fun onBindViewHolder(holder: POIHolder, position: Int) {
        holder.bindPOI(getItem(position))
    }
}