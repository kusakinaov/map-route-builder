package ku.olga.route_builder.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.list.PointsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.layoutFragment, PointsFragment())
                .commit()
        }
    }
}
