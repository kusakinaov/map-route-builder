package ku.olga.route_builder.presentation.user_points.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_point.view.*
import ku.olga.route_builder.R
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseAdapter
import javax.inject.Inject

class UserPointsAdapter @Inject constructor() :
    BaseAdapter<UserPoint, UserPointsAdapter.PointHolder>() {
    var onPointClickListener: ((UserPoint) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PointHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_point, parent, false)
        )

    override fun onBindViewHolder(holder: PointHolder, position: Int) {
        holder.point = getItem(position)
    }

    inner class PointHolder(view: View) : RecyclerView.ViewHolder(view) {
        var point: UserPoint? = null
            set(value) {
                field = value
                bindView()
            }

        init {
            view.setOnClickListener { point?.let { onPointClickListener?.invoke(it) } }
        }

        private fun bindView() {
            point?.let {
                itemView.textViewTitle.text = it.title
                itemView.textViewDescription.apply {
                    text = if (it.description?.isNotEmpty() == true) it.description else it.postalAddress
                    visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }
}