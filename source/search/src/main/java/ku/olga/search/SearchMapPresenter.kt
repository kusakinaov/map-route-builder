package ku.olga.search

import android.content.SharedPreferences
import ku.olga.ui_core.base.BaseLocationPresenter
import javax.inject.Inject

class SearchMapPresenter @Inject constructor(preferences: SharedPreferences) :
    BaseLocationPresenter<SearchMapView>(preferences) {

}