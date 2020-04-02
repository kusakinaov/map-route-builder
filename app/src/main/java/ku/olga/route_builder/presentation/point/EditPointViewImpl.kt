package ku.olga.route_builder.presentation.point

import android.view.View
import kotlinx.android.synthetic.main.fragment_edit_point.view.*

class EditPointViewImpl(val presenter: EditPointPresenter, val view: View) : EditPointView {

    override fun bindTitle(title: String) {
        view.editTextTitle.setText(title)
    }

    override fun bindDescription(description: String) {
        view.editTextDescription.setText(description)
    }

    override fun bindAddress(postalAddress: String) {
        view.textViewAddress.text = postalAddress
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}