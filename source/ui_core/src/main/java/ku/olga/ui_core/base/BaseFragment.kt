package ku.olga.ui_core.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import ku.olga.ui_core.FragmentContainer
import ku.olga.ui_core.R

abstract class BaseFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let { inject(it) }
    }

    abstract fun inject(activity: FragmentActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
    }

    open fun getTitle(resources: Resources) = ""

    open fun setTitle() {
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

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        activity?.let {
            if (it is FragmentContainer) {
                it.supportFragmentManager.beginTransaction()
                    .replace(it.getFragmentContainerId(), fragment)
                    .apply {
                        if (addToBackStack) {
                            addToBackStack(fragment::class.simpleName)
                        }
                    }.commit()
            }
        }
    }

    open fun getBackButtonRes() = R.drawable.ic_back

    open fun isPressBackConsumed() = false
}