package ku.olga.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_address.view.*
import ku.olga.core_api.dto.SearchAddress
import ku.olga.ui_core.base.BaseAdapter
import ku.olga.ui_core.utils.highlight
import javax.inject.Inject

class AddressesAdapter @Inject constructor() :
    BaseAdapter<SearchAddress, AddressesAdapter.AddressHolder>() {
    private var query: String? = null
    var onClickAddressListener: ((SearchAddress) -> Unit)? = null
    var highlightColor: Int = 0

    fun setQuery(query: String?) {
        if (this.query != query) {
            this.query = query ?: ""
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        )

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddressHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var address: SearchAddress? = null

        init {
            itemView.setOnClickListener { address?.let { onClickAddressListener?.invoke(it) } }
        }

        fun bind(address: SearchAddress?) {
            this.address = address
            itemView.textViewTitle.text = highlight(address?.postalAddress, query, highlightColor)
        }
    }
}