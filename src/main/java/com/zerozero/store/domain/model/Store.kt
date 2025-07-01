package com.zerozero.store.domain.model

import com.zerozero.core.domain.BaseEntity
import com.zerozero.image.domain.model.Image
import com.zerozero.store.domain.value.Address
import com.zerozero.store.domain.value.GeoLocation
import com.zerozero.store.presentation.request.CreateStoreRequest
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "store")
@SQLDelete(sql = "UPDATE store SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
class Store(

    @Column(name = "kakao_id", nullable = false)
    val kakaoId: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "category", nullable = false)
    val category: String,

    @Column(name = "phone", nullable = false)
    val phone: String,

    @Embedded
    val address: Address,

    @Embedded
    val geoLocation: GeoLocation,

    @Column(name = "status", nullable = false)
    var status: Boolean,

    @ElementCollection
    @CollectionTable(name = "store_images", joinColumns = [JoinColumn(name = "store_id")])
    val images: List<Image> = listOf(),

    @Column(name = "place_url", nullable = false)
    val placeUrl: String,

    @Column(name = "user_id", nullable = false)
    val userId: UUID
) : BaseEntity() {

    companion object {
        @JvmStatic
        fun create(userId: UUID, request: CreateStoreRequest): Store {
            return Store(
                kakaoId = request.kakaoId,
                name = request.placeName,
                category = request.category,
                phone = request.phone,
                address = Address(request.address, request.roadAddress),
                geoLocation = GeoLocation(request.longitude, request.latitude),
                status = true,
                images = request.images.map { Image(it) },
                placeUrl = request.placeUrl,
                userId = userId
            )
        }
    }
}
