package ku.olga.route_builder.presentation.user_points.list

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_points_list.view.*
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.point.EditPointFragment

class UserPointsListViewImpl(
        private val fragment: BaseFragment,
        private val presenter: UserPointsListPresenter
) : UserPointsListView {
    private val pointsAdapter = UserPointsAdapter().apply {
        onPointClickListener = {
            fragment.replaceFragment(EditPointFragment.newInstance(fragment, REQ_CODE_EDIT_POINT, it), true)
        }
    }

    init {
        fragment.view?.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = pointsAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        pointsAdapter.setItems(userPoints)
    }

    override fun showEmpty() {
        fragment.view?.apply {
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun showUserPoints() {
        fragment.view?.apply {
            textViewEmpty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}