package ku.olga.route_builder.presentation.base

interface BaseView {
    fun onAttach()
    fun onDetach()
    fun showProgress() {}
    fun hideProgress() {}
    fun showError(error: CharSequence) {}
    fun showDefaultError() {}
}