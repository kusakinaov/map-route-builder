package ku.olga.ui_core

interface BaseView {
    fun onAttach()
    fun onDetach()
    fun showProgress() {}
    fun hideProgress() {}
    fun showError(error: CharSequence) {}
    fun showDefaultError() {}
}