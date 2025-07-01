package com.zerozero.review.domain.model

import com.zerozero.core.domain.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.*

@Entity
@Table(name = "review")
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
class Review(
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "zero_drinks", nullable = false)
    var zeroDrinks: MutableSet<ZeroDrink> = mutableSetOf(),

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "store_id", nullable = false)
    val storeId: UUID,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "review_id")
    val reviewLikes: MutableList<ReviewLike> = mutableListOf()
) : BaseEntity() {

    fun isWrittenBy(userId: UUID): Boolean = this.userId == userId

    fun updateContent(content: String) {
        this.content = content
    }

    fun updateZeroDrinks(zeroDrinks: Set<ZeroDrink>) {
        this.zeroDrinks.clear()
        this.zeroDrinks.addAll(zeroDrinks)
    }

    companion object {
        @JvmStatic
        fun create(
            content: String,
            zeroDrinks: Set<ZeroDrink>,
            userId: UUID,
            storeId: UUID
        ): Review {
            return Review(
                content = content,
                zeroDrinks = zeroDrinks.toMutableSet(),
                userId = userId,
                storeId = storeId
            )
        }
    }
}
