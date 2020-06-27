package ku.olga.user_points.list

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_points_list.view.*
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.MoveItemCallback

class UserPointsListViewImpl(
    private val fragment: BaseFragment,
    private val presenter: UserPointsListPresenter,
    private val editPointMediator: EditPointMediator
) : UserPointsListView {
    private val pointsAdapter = UserPointsAdapter().apply {
        onPointClickListener = {
            val parent = fragment.parentFragment
            if (parent is BaseFragment) {
                editPointMediator.editPoint(parent, REQ_CODE_EDIT_POINT, it)
            }
        }
        onOrderChangeListener = {
            presenter.onOrderChanged(it)
        }
    }
    private val itemTouchHelper = ItemTouchHelper(MoveItemCallback(pointsAdapter))

    init {
        fragment.view?.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = pointsAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        pointsAdapter.setItems(userPoints)
        if (pointsAdapter.itemCount > 0) {
            showUserPoints()
        } else {
            showEmpty()
        }
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

    override fun invalidateUserPoints() {
        fragment.parentFragment?.let {
            if (it is UserPointsListFragment.OnOrderChangeCallback) {
                it.onOrderChanged()
            }
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}