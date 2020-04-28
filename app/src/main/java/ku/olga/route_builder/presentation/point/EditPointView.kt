package ku.olga.route_builder.presentation.point

import ku.olga.route_builder.presentation.base.BaseView

interface EditPointView : BaseView {
    fun bindTitle(title: String)
    fun bindDescription(description: String)
    fun bindAddress(postalAddress: String)
    fun bindSaveButton(enabled: Boolean)
    fun notifySaveSuccessful()
}