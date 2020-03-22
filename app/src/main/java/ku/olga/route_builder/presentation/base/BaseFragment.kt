package ku.olga.route_builder.presentation.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        setTitle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    open fun getTitle(resources: Resources) = ""

    private fun setTitle() {
        activity?.let {
            it.title = getTitle(it.resources)
        }
    }

    fun showSnackbar(message: CharSequence?) {
        message?.let { m ->
            view?.let {
                Snackbar.make(it, m, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}