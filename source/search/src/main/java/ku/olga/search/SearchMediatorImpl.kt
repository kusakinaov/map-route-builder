package ku.olga.search

import androidx.fragment.app.Fragment
import ku.olga.core_api.mediator.SearchMediator
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject

class SearchMediatorImpl @Inject constructor(): SearchMediator {
    override fun openSearch(target: Fragment, requestCode: Int) {
        if (target is BaseFragment) {
            target.replaceFragment(SearchAddressesFragment.newInstance(target, requestCode), true)
        }
    }

    override fun openSearchMap(target: Fragment, requestCode: Int) {
        if (target is BaseFragment) {
            target.replaceFragment(SearchMapFragment.newInstance(target, requestCode), true)
        }
    }
}