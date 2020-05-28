package ku.olga.route_builder.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ku.olga.route_builder.R
import ku.olga.ui_core.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.ActivityComponent
import ku.olga.route_builder.presentation.dagger.component.DaggerActivityComponent
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment
import ku.olga.ui_core.FragmentContainer

class MainActivity : AppCompatActivity(), FragmentContainer {
    private var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        activityComponent = DaggerActivityComponent.builder()
            .facadeComponent(App.application.facadeComponent)
            .build()

        supportFragmentManager.addOnBackStackChangedListener { bindBackStack() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(getFragmentContainerId(), UserPointsFragment.newInstance()).commit()
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

    fun getActivityComponent() = activityComponent

    override fun onDestroy() {
        super.onDestroy()
        activityComponent = null
    }

    override fun getFragmentContainerId(): Int = R.id.layoutFragment
}
