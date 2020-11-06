package ku.olga.edit_point

import android.text.Editable
import android.view.View
import kotlinx.android.synthetic.main.fragment_edit_point.view.*
import ku.olga.ui_core.view.SimpleTextWatcher

abstract class EditPointViewImpl(
    private val view: View,
    private val presenter: EditPointPresenter
) : EditPointView {
    init {
        view.apply {
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
        view.editTextTitle?.setText(title)
    }

    override fun bindDescription(description: String) {
        view.editTextDescription?.setText(description)
    }

    override fun bindAddress(postalAddress: String) {
        view.textViewAddress?.text = postalAddress
    }

    override fun setDeleteButtonVisibility(visible: Boolean) {
        view.buttonDelete?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun showConfirmationDeleteDialog(title: String) {
        openConfirmation(title)
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }

    abstract fun openConfirmation(title: String)
}