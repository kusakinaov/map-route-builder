package ku.olga.core_api.mediator

import androidx.fragment.app.Fragment
import ku.olga.core_api.dto.Category

interface CategoryMediator {
    fun openCategory(target: Fragment, requestCode: Int, category: Category)
}