package ku.olga.route_builder.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_point.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseAdapter

class UserPointsAdapter : BaseAdapter<UserPoint, UserPointsAdapter.PointHolder>() {
    var onPointClickListener: ((UserPoint) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PointHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_point, parent, false))

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
                itemView.textViewDescription.text = it.description
            }
        }
    }
}