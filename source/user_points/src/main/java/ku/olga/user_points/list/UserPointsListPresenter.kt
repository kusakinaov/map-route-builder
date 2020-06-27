package ku.olga.user_points.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.repository.PointsCacheRepository
import ku.olga.ui_core.base.BasePresenter
import javax.inject.Inject

class UserPointsListPresenter @Inject constructor(private val userPointsCacheRepository: PointsCacheRepository) :
    BasePresenter<UserPointsListView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsListView) {
        super.attachView(view)
        view.setUserPoints(userPoints)
    }

    fun setUserPoints(userPoints: List<UserPoint>) {
        this.userPoints.clear()
        this.userPoints.addAll(userPoints)

        view?.setUserPoints(userPoints)
    }

    fun onOrderChanged(list: List<UserPoint>) {
        CoroutineScope(Dispatchers.IO).launch {
            userPointsCacheRepository.saveOrder(list)
            withContext(Dispatchers.Main) {
                view?.invalidateUserPoints()
            }
        }
    }
}