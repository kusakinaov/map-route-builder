package ku.olga.route_builder.presentation.search.list

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_address.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseAdapter
import javax.inject.Inject

class AddressesAdapter @Inject constructor() :
    BaseAdapter<SearchAddress, AddressesAdapter.AddressHolder>() {
    private var query: String? = null
    var onClickAddressListener: ((SearchAddress) -> Unit)? = null
    var highlightColor: Int = 0

    fun setQuery(query: String?) {
        if (this.query == query) return

        this.query = query
        notifyDataSetChanged()
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
            itemView.textViewTitle.text = SpannableStringBuilder(address?.postalAddress).apply {
                query?.split("\\s+".toRegex())?.forEach {
                    val index = indexOf(it)
                    if (index >= 0) {
                        setSpan(
                            ForegroundColorSpan(highlightColor),
                            index,
                            index + it.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
    }
}