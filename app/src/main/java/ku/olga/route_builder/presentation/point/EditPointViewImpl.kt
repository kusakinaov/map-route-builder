package ku.olga.route_builder.presentation.point

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_edit_point.view.*
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.view.SimpleTextWatcher

class EditPointViewImpl(
    val fragment: BaseFragment,
    private val presenter: EditPointPresenter,
    val view: View
) : EditPointView {
    init {
        view.apply {
            textViewSave.setOnClickListener { presenter.onClickSave() }
            editTextTitle.addTextChangedListener(object : SimpleTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.setTitle(s?.toString() ?: "")
                }
            })
            editTextDescription.addTextChangedListener(object : SimpleTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.setDescription(s?.toString() ?: "")
                }
            })
        }
    }

    override fun bindTitle(title: String) {
        view.editTextTitle.setText(title)
    }

    override fun bindDescription(description: String) {
        view.editTextDescription.setText(description)
    }

    override fun bindAddress(postalAddress: String) {
        view.textViewAddress.text = postalAddress
    }

    override fun bindSaveButton(enabled: Boolean) {
        view.textViewSave.isEnabled = enabled
    }

    override fun notifySaveSuccessful() {
        fragment.fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}