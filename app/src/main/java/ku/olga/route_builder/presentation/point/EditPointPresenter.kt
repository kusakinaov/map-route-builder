package ku.olga.route_builder.presentation.point

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BasePresenter

class EditPointPresenter : BasePresenter<EditPointView>() {
    private var point: UserPoint? = null
        set(value) {
            field = value
            bindUserPoint()
        }

    fun setAddress(postalAddress: String?, lat: Double, lon: Double) {
        point = UserPoint(postalAddress = postalAddress, lat = lat, lon = lon)
    }

    override fun attachView(view: EditPointView) {
        super.attachView(view)
        bindUserPoint()
    }

    override fun detachView() {
        super.detachView()
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
        TODO("Not yet implemented")
    }
}