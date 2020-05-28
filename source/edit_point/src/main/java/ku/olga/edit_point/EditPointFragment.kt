package ku.olga.edit_point

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ku.olga.core_api.AppWithFacade
import ku.olga.ui_core.REQ_CODE_CONFIRM_DELETE_POINT
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject

class EditPointFragment : BaseFragment() {
    @Inject
    lateinit var presenter: EditPointPresenter

    private var editPointView: EditPointView? = null

    override fun inject(activity: FragmentActivity) {
        activity.application?.let {
            if (it is AppWithFacade) {
                EditPointComponent.build(it.getFacade()).inject(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            presenter.setPoint(it.getSerializable(USER_POINT) as UserPoint)
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
        private const val USER_POINT = "user_point"

        fun newInstance(target: Fragment, requestCode: Int, userPoint: UserPoint) =
            EditPointFragment().apply {
                setTargetFragment(target, requestCode)
                arguments = Bundle().apply { putSerializable(USER_POINT, userPoint) }
            }
    }
}