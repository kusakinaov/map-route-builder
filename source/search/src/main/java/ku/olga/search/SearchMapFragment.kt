package ku.olga.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.ui_core.base.BaseFragment

class SearchMapFragment : BaseFragment() {

    override fun inject(activity: FragmentActivity) {
        SearchComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) = SearchMapFragment().apply {
            setTargetFragment(target, requestCode)
        }
    }
}