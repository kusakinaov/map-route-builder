package ku.olga.route_builder.presentation.list

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_points.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.map.MapFragment

class PointsFragment : BaseFragment() {
    private val pointsAdapter = PointsAdapter().apply {
        onPointClickListener = { showSnackbar(it.title) }
    }

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_point_list)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.points, menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = pointsAdapter
        }
        buttonAdd.setOnClickListener { showSnackbar("add point") }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionMap -> {
            openMap()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openMap() {
        if (activity is MainActivity) {
            (activity as MainActivity).replaceFragment(MapFragment(), true)
        }
    }
}