package ku.olga.core_api.mediator

import androidx.fragment.app.Fragment

interface SearchMediator {
    fun openSearch(target: Fragment, requestCode: Int)
    fun openSearchMap(target: Fragment, requestCode: Int)
}