package ku.olga.route_builder.presentation.point

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_CONFIRM_DELETE_POINT
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.DaggerPointComponent
import ku.olga.route_builder.presentation.dagger.component.PointComponent
import ku.olga.route_builder.presentation.dagger.module.ApplicationModule
import javax.inject.Inject

class EditPointFragment : BaseFragment() {
    private lateinit var pointComponent: PointComponent

    @Inject
    lateinit var presenter: EditPointPresenter

    private var editPointView: EditPointView? = null

    override fun inject() {
        pointComponent =
            DaggerPointComponent.builder().applicationModule(ApplicationModule(App.application))
                .build()
        pointComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(USER_POINT)) {
                presenter.setPoint(it.getSerializable(USER_POINT) as UserPoint)
            } else {
                presenter.setAddress(
                    it.getString(ADDRESS),
                    it.getDouble(LATITUDE),
                    it.getDouble(LONGITUDE)
                )
            }
        }
    }

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_edit)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_edit_point, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editPointView = EditPointViewImpl(this, presenter)
        editPointView?.onAttach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.edit_point, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.actionSave).isEnabled = presenter.isSaveEnabled()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actionSave -> {
            presenter.onClickSave()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_CONFIRM_DELETE_POINT) {
                presenter.onDeleteConfirmed()
            }
        }
    }

    override fun onDestroyView() {
        editPointView?.onDetach()
        super.onDestroyView()
    }

    override fun getBackButtonRes() = R.drawable.ic_close

    companion object {
        private const val ADDRESS = "address"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val USER_POINT = "user_point"

        fun newInstance(
            target: Fragment,
            requestCode: Int,
            postalAddress: String,
            lat: Double,
            lon: Double
        ) =
            EditPointFragment().apply {
                setTargetFragment(target, requestCode)
                arguments = Bundle().apply {
                    putString(ADDRESS, postalAddress)
                    putDouble(LATITUDE, lat)
                    putDouble(LONGITUDE, lon)
                }
            }

        fun newInstance(target: Fragment, requestCode: Int, userPoint: UserPoint) =
            EditPointFragment().apply {
                setTargetFragment(target, requestCode)
                arguments = Bundle().apply { putSerializable(USER_POINT, userPoint) }
            }
    }
}