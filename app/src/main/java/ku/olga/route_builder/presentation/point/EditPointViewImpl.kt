package ku.olga.route_builder.presentation.point

import android.text.Editable
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_edit_point.view.*
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.view.SimpleTextWatcher

class EditPointViewImpl(
    val fragment: BaseFragment,
    private val presenter: EditPointPresenter
) : EditPointView {
    init {
        fragment.view?.apply {
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
        fragment.view?.editTextTitle?.setText(title)
    }

    override fun bindDescription(description: String) {
        fragment.view?.editTextDescription?.setText(description)
    }

    override fun bindAddress(postalAddress: String) {
        fragment.view?.textViewAddress?.text = postalAddress
    }

    override fun notifySaveSuccessful() {
        fragment.fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun invalidateOptionsMenu() {
        fragment.activity?.invalidateOptionsMenu()
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}