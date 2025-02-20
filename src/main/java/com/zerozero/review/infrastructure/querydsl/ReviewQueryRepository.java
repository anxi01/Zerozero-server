package com.zerozero.review.infrastructure.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.model.ReviewFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.zerozero.review.domain.model.QReview.review;
import static com.zerozero.review.domain.model.QReviewLike.reviewLike;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Review> findByStoreIdAndFilter(UUID storeId, ReviewFilter reviewFilter) {
        JPAQuery<Review> query = queryFactory.selectFrom(review)
                .leftJoin(review.reviewLikes, reviewLike)
                .fetchJoin()
                .where(
                        eqStoreId(storeId),
                        isDeleted(false)
                )
                .distinct();

        if (reviewFilter == null || reviewFilter == ReviewFilter.RECENT) {
            query.orderBy(review.createdAt.desc());
        } else if (reviewFilter == ReviewFilter.RECOMMEND) {
            query.orderBy(review.reviewLikes.size().desc(), review.createdAt.desc());
        }

        return query.fetch();
    }

    private BooleanExpression eqStoreId(UUID storeId) {
        return review.storeId.eq(storeId);
    }

    private BooleanExpression isDeleted(boolean deleted) {
        return review.deleted.eq(deleted);
    }
}
