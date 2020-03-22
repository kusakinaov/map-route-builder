package ku.olga.route_builder.presentation.list

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_points.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class PointsFragment : BaseFragment() {
    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_point_list)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
        }
        buttonAdd.setOnClickListener { showSnackbar("add point") }
    }
}