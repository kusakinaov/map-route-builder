package ku.olga.route_builder.presentation.search

import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_address.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseAdapter

class SearchPointsAdapter : BaseAdapter<Address, SearchPointsAdapter.AddressHolder>() {
    var onClickAddressListener: ((Address) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        )

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        holder.address = getItem(position)
    }

    inner class AddressHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address: Address? = null
            set(value) {
                field = value
                bindAddress()
            }

        init {
            itemView.setOnClickListener { address?.let { onClickAddressListener?.invoke(it) } }
        }

        private fun bindAddress() {
            itemView.apply {
                textViewTitle.text = address?.getAddressLine(0)
                textViewDescription.text = address?.thoroughfare
            }
        }
    }
}