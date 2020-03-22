package ku.olga.route_builder.presentation.list

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_points.*
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_SEARCH_POINT
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.map.MapFragment
import ku.olga.route_builder.presentation.search.SearchPointsFragment

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
        buttonAdd.setOnClickListener {
            replaceFragment(SearchPointsFragment.newInstance(this, REQ_CODE_SEARCH_POINT))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionMap -> {
            openMap()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SEARCH_POINT) {
                TODO()
            }
        }
    }

    private fun openMap() {
        replaceFragment(MapFragment())
    }
}