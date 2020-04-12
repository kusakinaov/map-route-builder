package ku.olga.route_builder.presentation.point

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class EditPointPresenter(private val pointsRepository: PointsRepository) : BasePresenter<EditPointView>() {
    private var point: UserPoint? = null
        set(value) {
            field = value
            bindUserPoint()
        }
    private var job: Job? = null
    var title: String = ""
        set(value) {
            field = value
            bindSaveButton()
        }

    var description: String = ""
        set(value) {
            field = value
            bindSaveButton()
        }

    fun setAddress(postalAddress: String?, lat: Double, lon: Double) {
        point = UserPoint(postalAddress = postalAddress, lat = lat, lon = lon)
        point?.let {
            title = it.title ?: ""
            description = it.description ?: ""
        }
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
            val id = pointsRepository.saveUserPoint(it)
            withContext(Dispatchers.Main) {
                view?.notifySaveSuccessful()
            }
        }
    }


    private fun bindSaveButton() {
        view?.bindSaveButton(title.isNotEmpty())
    }
}