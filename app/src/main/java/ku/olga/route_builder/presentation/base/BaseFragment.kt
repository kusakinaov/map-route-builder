package ku.olga.route_builder.presentation.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.MainActivity

abstract class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
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

    fun replaceFragment(fragment: Fragment) {
        replaceFragment(fragment, true)
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).replaceFragment(fragment, addToBackStack)
        }
    }

    open fun getBackButtonRes() = R.drawable.ic_back
}