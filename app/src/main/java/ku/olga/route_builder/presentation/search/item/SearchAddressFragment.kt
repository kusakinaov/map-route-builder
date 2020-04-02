package ku.olga.route_builder.presentation.search.item

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment

class SearchAddressFragment : BaseFragment() {
    private var searchAddressView: SearchAddressView? = null
    private val searchAddressPresenter = SearchAddressPresenter(App.pointsRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchAddressPresenter.searchAddress = it.getSerializable(SEARCH_ADDRESS) as SearchAddress?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_search_address, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAddressView = SearchAddressViewImpl(this, view).apply {
            presenter = searchAddressPresenter
            onCreate(savedInstanceState)
        }
        searchAddressView?.onAttach()
    }

    override fun onStart() {
        super.onStart()
        searchAddressView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        searchAddressView?.onResume()
    }

    override fun onPause() {
        searchAddressView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        searchAddressView?.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        searchAddressView?.onDestroy()
        searchAddressView?.onDetach()
        super.onDestroyView()
    }

    override fun getTitle(resources: Resources) = resources.getString(R.string.title_search)

    companion object {
        private const val SEARCH_ADDRESS = "search_address"

        fun newInstance(target: Fragment, requestCode: Int, searchAddress: SearchAddress) =
                SearchAddressFragment().apply {
                    setTargetFragment(target, requestCode)
                    arguments = Bundle().apply {
                        putSerializable(SEARCH_ADDRESS, searchAddress)
                    }
                }
    }
}