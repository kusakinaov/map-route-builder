package ku.olga.edit_point

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint

class EditPointDialog : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            EditPointComponent.build((it.application as AppWithFacade).getFacade()).inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.layout_edit_point, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val USER_POINT = "user_point"
        private val TAG = EditPointDialog::class.simpleName

        fun show(target: Fragment, requestCode: Int, userPoint: UserPoint) {
            dismiss(target.fragmentManager)
            target.fragmentManager?.apply {
                if (!isStateSaved) {
                    EditPointDialog().apply {
                        setTargetFragment(target, requestCode)
                        arguments = Bundle().apply { putSerializable(USER_POINT, TAG) }
                    }.show(this, TAG)
                }
            }
        }

        private fun dismiss(fragmentManager: FragmentManager?) {
            fragmentManager?.apply {
                if (!isStateSaved) {
                    findFragmentByTag(TAG)?.let { if (it is DialogFragment) it.dismiss() }
                }
            }
        }
    }
}