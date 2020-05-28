package ku.olga.category

import androidx.fragment.app.Fragment
import ku.olga.core_api.dto.Category
import ku.olga.core_api.mediator.CategoryMediator
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject

class CategoryMediatorImpl @Inject constructor() : CategoryMediator {
    override fun openCategory(target: Fragment, requestCode: Int, category: Category) {
        if (target is BaseFragment) {
            target.replaceFragment(CategoryFragment.newInstance(target, requestCode, category), true)
        }
    }
}