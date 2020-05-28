package ku.olga.route_builder.presentation.point

import ku.olga.ui_core.BaseView

interface EditPointView : BaseView {
    fun bindTitle(title: String)
    fun bindDescription(description: String)
    fun bindAddress(postalAddress: String)
    fun setDeleteButtonVisibility(visible: Boolean)
    fun notifyCreateSuccessful()
    fun notifyEditSuccessful()
    fun invalidateOptionsMenu()
    fun showConfirmationDeleteDialog(title: String)
    fun notifyDeleteSuccessful()
}