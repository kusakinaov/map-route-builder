package ku.olga.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.mediator.UserPointsMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.FragmentContainer
import ku.olga.ui_core.utils.OnKeyboardVisibilityListener
import ku.olga.ui_core.utils.setKeyboardListener
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FragmentContainer, OnKeyboardVisibilityListener {
    @Inject
    lateinit var userPointsMediator: UserPointsMediator

    override val fragmentContainerId: Int
        get() = R.id.layoutFragment

    private val fragment: Fragment?
        get() = supportFragmentManager.findFragmentById(fragmentContainerId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setKeyboardListener(this, coordinatorLayout, this)

        ActivityComponent.build((application as AppWithFacade).getFacade()).inject(this)

        supportFragmentManager.addOnBackStackChangedListener { bindBackStack() }
        if (savedInstanceState == null) {
            userPointsMediator.openUserPoints(supportFragmentManager, fragmentContainerId)
        }
    }

    private fun bindBackStack() {
        supportActionBar?.apply {
            val entryCount = supportFragmentManager.backStackEntryCount
            setDisplayHomeAsUpEnabled(entryCount > 0)

            fragment?.let {
                if (it is BaseFragment) {
                    setBackButton(it.getBackButtonRes())
                } else {
                    setBackButton(R.drawable.ic_back)
                }
            }
        }
    }

    private fun setBackButton(drawableRes: Int) {
        supportActionBar?.setHomeAsUpIndicator(drawableRes)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        fragment.let {
            if (it is OnKeyboardVisibilityListener) {
                it.onKeyboardVisibilityChanged(isVisible)
            }
        }
    }
}
