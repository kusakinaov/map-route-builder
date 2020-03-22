package ku.olga.route_builder.presentation.base

interface BaseView {
    fun showProgress()
    fun hideProgress()
    fun showError(error: CharSequence)
}