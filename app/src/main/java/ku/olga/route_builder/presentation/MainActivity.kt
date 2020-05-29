package ku.olga.route_builder.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.mediator.UserPointsMediator
import ku.olga.route_builder.R
import ku.olga.ui_core.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.ActivityComponent
import ku.olga.route_builder.presentation.dagger.component.DaggerActivityComponent
import ku.olga.user_points.root.UserPointsFragment
import ku.olga.ui_core.FragmentContainer
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FragmentContainer {
    @Inject
    lateinit var userPointsMediator: UserPointsMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(layoutAppBar.toolbar)

        ActivityComponent.build((application as AppWithFacade).getFacade()).inject(this)

        supportFragmentManager.addOnBackStackChangedListener { bindBackStack() }
        if (savedInstanceState == null) {
            userPointsMediator.openUserPoints(supportFragmentManager, getFragmentContainerId())
        }
    }

    private fun bindBackStack() {
        supportActionBar?.apply {
            val entryCount = supportFragmentManager.backStackEntryCount
            setDisplayHomeAsUpEnabled(entryCount > 0)

            val fragment = getFragment()
            if (fragment is BaseFragment) {
                setBackButton(fragment.getBackButtonRes())
            } else {
                setBackButton(R.drawable.ic_back)
            }
        }
    }

    private fun setBackButton(drawableRes: Int) {
        supportActionBar?.setHomeAsUpIndicator(drawableRes)
    }

    private fun getFragment() = supportFragmentManager.findFragmentById(getFragmentContainerId())

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        getFragment().let {
            if (!(it is BaseFragment && it.isPressBackConsumed())) {
                super.onBackPressed()
            }
        }
    }

    override fun getFragmentContainerId(): Int = R.id.layoutFragment
}
