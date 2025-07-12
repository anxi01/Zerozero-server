package com.zerozero.store.infrastructure.mongodb

import com.zerozero.core.util.GeoJsonConverter
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "store")
class Store(
    val storeId: UUID,
    val kakaoId: String,
    val name: String,
    val category: String,
    val phone: String,
    val address: String,
    val roadAddress: String,
    val longitude: String,
    val latitude: String,
    val location: GeoJsonPoint,
    val placeUrl: String,
    val status: Boolean,
    val userId: UUID,
) {
    companion object {
        fun from(store: com.zerozero.store.domain.model.Store): Store {
            return Store(
                storeId = store.id,
                kakaoId = store.kakaoId,
                name = store.name,
                category = store.category,
                phone = store.phone,
                address = store.address.address,
                roadAddress = store.address.roadAddress,
                longitude = store.geoLocation.longitude,
                latitude = store.geoLocation.latitude,
                location = GeoJsonConverter.of(
                    store.geoLocation.longitude,
                    store.geoLocation.latitude
                ),
                placeUrl = store.placeUrl,
                status = true,
                userId = store.userId
            )
        }
    }
}
