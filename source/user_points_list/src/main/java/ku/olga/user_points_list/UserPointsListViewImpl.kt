package ku.olga.user_points_list

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_points_list.view.*
import ku.olga.core_api.dto.UserPoint

abstract class UserPointsListViewImpl(
    private val view: View,
    private val presenter: UserPointsListPresenter
) : UserPointsListView {
    private val pointsAdapter = UserPointsAdapter().apply {
        onPointClickListener = { editPoint(it) }
        onOrderChangeListener = { presenter.onOrderChanged(it) }
    }
    private val itemTouchHelper = ItemTouchHelper(MoveItemCallback(pointsAdapter))

    init {
        view.apply {
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
        view.apply {
            recyclerView.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun showUserPoints() {
        view.apply {
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

    abstract fun editPoint(userPoint: UserPoint)
}