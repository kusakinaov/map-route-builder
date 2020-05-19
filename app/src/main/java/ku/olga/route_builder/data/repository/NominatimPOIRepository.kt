package ku.olga.route_builder.data.repository

import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.repository.POIRepository
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.util.BoundingBox
import ku.olga.route_builder.domain.model.POI as AppPOI

class NominatimPOIRepository(private val poiProvider: NominatimPOIProvider) : POIRepository {
    override suspend fun getPOIs(boundingBox: BoundingBox, category: Category): List<AppPOI> =
        poiProvider.getPOIInside(boundingBox, category.key, 100).map { it.toAppPOI() }.toList()

    fun POI.toAppPOI() = AppPOI(mId,
        mLocation.latitude,
        mLocation.longitude,
        mType,
        mDescription,
        mThumbnailPath,
        mUrl,
        mRank)
}