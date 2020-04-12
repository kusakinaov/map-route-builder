package ku.olga.route_builder.presentation.list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_points.view.*
import ku.olga.route_builder.REQ_CODE_SEARCH_POINT
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.map.MapFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment

class UserPointsViewImpl(
    private val fragment: BaseFragment,
    view: View,
    private val presenter: UserPointsPresenter
) :
    UserPointsView {
    private val pointsAdapter = UserPointsAdapter().apply {
        onPointClickListener = {

        }
    }

    init {
        view.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = pointsAdapter
            }
            buttonAdd.setOnClickListener {
                fragment.replaceFragment(
                    SearchAddressesFragment.newInstance(
                        fragment,
                        REQ_CODE_SEARCH_POINT
                    )
                )
            }
        }
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        pointsAdapter.setItems(userPoints)
    }

    override fun onClickOpenMap() {
        fragment.replaceFragment(MapFragment())
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}