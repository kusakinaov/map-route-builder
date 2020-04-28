package ku.olga.route_builder.presentation.point

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.services.PointsService
import ku.olga.route_builder.presentation.base.BasePresenter

class EditPointPresenter(private val pointsService: PointsService) : BasePresenter<EditPointView>() {
    private var point: UserPoint? = null
    private var job: Job? = null
    private var title: String = ""
    private var description: String = ""

    fun setAddress(postalAddress: String?, lat: Double, lon: Double) {
        point = UserPoint(postalAddress = postalAddress, lat = lat, lon = lon)
        point?.let {
            setTitle(it.title ?: "")
            setDescription(it.description ?: "")
        }
    }

    fun setPoint(point: UserPoint) {
        this.point = point
        view?.invalidateOptionsMenu()
    }

    fun setTitle(title: String) {
        this.title = title
        view?.invalidateOptionsMenu()
    }

    fun setDescription(description: String) {
        this.description = description
    }

    override fun attachView(view: EditPointView) {
        super.attachView(view)
        bindUserPoint()
    }

    private fun bindUserPoint() {
        view?.apply {
            point?.let {
                bindTitle(it.title ?: "")
                bindDescription(it.description ?: "")
                bindAddress(it.postalAddress ?: "")
            }
        }
    }

    fun onClickSave() {
        point?.title = title
        point?.description = description
        job = saveUserPoint()
    }

    private fun saveUserPoint() = CoroutineScope(Dispatchers.IO).launch {
        point?.let {
            val id = pointsService.saveUserPoint(it)
            withContext(Dispatchers.Main) {
                view?.notifySaveSuccessful()
            }
        }
    }

    fun isSaveEnabled() = title.isNotEmpty()
}