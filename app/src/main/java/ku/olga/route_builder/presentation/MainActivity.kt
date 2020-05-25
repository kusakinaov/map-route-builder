package ku.olga.route_builder.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.dagger.component.ActivityComponent
import ku.olga.route_builder.presentation.dagger.component.DaggerActivityComponent
import ku.olga.route_builder.presentation.user_points.root.UserPointsFragment

class MainActivity : AppCompatActivity() {
    private var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        activityComponent = DaggerActivityComponent.builder()
            .applicationComponent(App.application.applicationComponent)
            .build()

        supportFragmentManager.addOnBackStackChangedListener { bindBackStack() }
        if (savedInstanceState == null) {
            replaceFragment(UserPointsFragment.newInstance(), false)
        }
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.layoutFragment, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.simpleName)
        }
        transaction.commit()
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

    fun setBackButton(drawableRes: Int) {
        supportActionBar?.setHomeAsUpIndicator(drawableRes)
    }

    private fun getFragment() = supportFragmentManager.findFragmentById(R.id.layoutFragment)

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
}
