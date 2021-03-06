package ku.olga.edit_point

import kotlinx.coroutines.*
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.dto.UserPointType
import ku.olga.core_api.repository.PointsCacheRepository
import ku.olga.ui_core.base.BasePresenter
import javax.inject.Inject

class EditPointPresenter @Inject constructor(private val pointsRepository: PointsCacheRepository) : BasePresenter<EditPointView>() {
    private var point: UserPoint? = null
    private var job: Job? = null
    private var title: String = ""
    private var description: String = ""

    fun setAddress(postalAddress: String?, lat: Double, lon: Double) {
        point = UserPoint(
            postalAddress = postalAddress,
            lat = lat,
            lon = lon,
            type = UserPointType.ADDRESS
        )
        point?.let {
            setTitle(it.title ?: "")
            setDescription(it.description ?: "")
        }
    }

    fun setPoint(point: UserPoint) {
        this.point = point
        this.title = point.title ?: ""
        this.description = point.description ?: ""
    }

    fun setTitle(title: String) {
        this.title = title
        view?.invalidateOptionsMenu()
    }

    fun setDescription(description: String) {
        this.description = description
        view?.invalidateOptionsMenu()
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

                setDeleteButtonVisibility(!it.isNew)
            }
        }
    }

    fun onClickSave() {
        point?.title = title
        point?.description = description
        if (point?.isNew == true) {
            job = createUserPoint()
        } else {
            job = editUserPoint()
        }
    }

    private fun createUserPoint() = CoroutineScope(Dispatchers.IO).launch {
        point?.let {
            val id = pointsRepository.saveUserPoint(it)
            withContext(Dispatchers.Main) {
                view?.notifyCreateSuccessful()
            }
        }
    }

    private fun editUserPoint() = CoroutineScope(Dispatchers.IO).launch {
        point?.let {
            val id = pointsRepository.saveUserPoint(it)
            withContext(Dispatchers.Main) {
                view?.notifyEditSuccessful()
            }
        }
    }

    fun isSaveEnabled() = title.isNotEmpty()
            && (title != point?.title || description != point?.description)

    fun onClickDelete() {
        view?.showConfirmationDeleteDialog(title)
    }

    fun onDeleteConfirmed() {
        job = deleteUserPoint()
    }

    private fun deleteUserPoint() = CoroutineScope(Dispatchers.IO).launch {
        point?.let {
            val id = pointsRepository.deleteUserPoint(it)
            withContext(Dispatchers.Main) {
                view?.notifyDeleteSuccessful()
            }
        }
    }
}