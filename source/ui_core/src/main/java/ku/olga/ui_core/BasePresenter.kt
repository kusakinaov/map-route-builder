package ku.olga.ui_core

abstract class BasePresenter<T : BaseView> {
    var view: T? = null

    open fun attachView(view: T) {
        this.view = view
    }

    open fun detachView() {
        view = null
    }

    fun showProgress() {
        view?.showProgress()
    }

    fun hideProgress() {
        view?.hideProgress()
    }

    fun showError(message: CharSequence?) {
        message?.let { view?.showError(it) }
    }
}