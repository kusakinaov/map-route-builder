package ku.olga.route_builder.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.list.PointsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener { bindBackStack() }
        if (savedInstanceState == null) {
            replaceFragment(PointsFragment(), false)
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

    fun getFragment() = supportFragmentManager.findFragmentById(R.id.layoutFragment)

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
