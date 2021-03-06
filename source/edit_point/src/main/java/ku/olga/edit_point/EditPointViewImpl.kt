package ku.olga.edit_point

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_edit_point.view.*
import ku.olga.core_api.mediator.ConfirmationMediator
import ku.olga.ui_core.REQ_CODE_CONFIRM_DELETE_POINT
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.view.SimpleTextWatcher

class EditPointViewImpl(
    private val fragment: BaseFragment,
    private val presenter: EditPointPresenter,
    private val confirmationMediator: ConfirmationMediator
) : EditPointView {
    init {
        fragment.view?.apply {
            editTextTitle.addTextChangedListener(object :
                SimpleTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.setTitle(s?.toString() ?: "")
                }
            })
            editTextDescription.addTextChangedListener(object :
                SimpleTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.setDescription(s?.toString() ?: "")
                }
            })
            buttonDelete.setOnClickListener { presenter.onClickDelete() }
        }
    }

    override fun bindTitle(title: String) {
        fragment.view?.editTextTitle?.setText(title)
    }

    override fun bindDescription(description: String) {
        fragment.view?.editTextDescription?.setText(description)
    }

    override fun bindAddress(postalAddress: String) {
        fragment.view?.textViewAddress?.text = postalAddress
    }

    override fun setDeleteButtonVisibility(visible: Boolean) {
        fragment.view?.buttonDelete?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun notifyCreateSuccessful() {
        fragment.fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun notifyEditSuccessful() {
        fragment.apply {
            fragmentManager?.popBackStack()
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent())
        }
    }

    override fun invalidateOptionsMenu() {
        fragment.activity?.invalidateOptionsMenu()
    }

    override fun showConfirmationDeleteDialog(title: String) {
        confirmationMediator.showConfirmation(
            fragment,
            REQ_CODE_CONFIRM_DELETE_POINT,
            fragment.getString(R.string.question_confirm_delete_point, title),
            Bundle()
        )
    }

    override fun notifyDeleteSuccessful() {
        fragment.fragmentManager?.let {
            if (it.isStateSaved) return

            it.popBackStack()
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}