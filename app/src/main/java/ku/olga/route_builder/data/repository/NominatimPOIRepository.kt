package ku.olga.route_builder.data.repository

import ku.olga.core_api.dto.Category
import ku.olga.route_builder.domain.repository.POIRepository
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import ku.olga.core_api.dto.POI as AppPOI
import ku.olga.route_builder.domain.model.BoundingBox
import org.osmdroid.util.BoundingBox as ApiBoundingBox

class NominatimPOIRepository(private val poiProvider: NominatimPOIProvider) : POIRepository {
    override suspend fun getPOIs(boundingBox: BoundingBox, category: Category): List<AppPOI> =
        poiProvider.getPOIInside(boundingBox.toApiBoundingBox(), category.key, 100)
            ?.map { it.toAppPOI() }?.toList() ?: emptyList()

    private fun POI.toAppPOI() = AppPOI(mId,
        mLocation.latitude,
        mLocation.longitude,
        "",
        mDescription,
        mThumbnailPath,
        mUrl,
        mRank,
        mCategory,
        mType)

    private fun BoundingBox.toApiBoundingBox() =
        ApiBoundingBox(latNorth, lonEast, latSouth, lonWest)
}