package ku.olga.route_builder.presentation.base

class BasePresenter<T : BaseView> {
    var view: T? = null

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
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