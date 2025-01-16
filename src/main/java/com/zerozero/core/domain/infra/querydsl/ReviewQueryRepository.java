package com.zerozero.core.domain.infra.querydsl;

import static com.zerozero.core.domain.entity.QReview.review;
import static com.zerozero.core.domain.entity.QReviewLike.reviewLike;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Review.Filter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<Review> findByStoreIdAndFilter(UUID storeId, Filter filter) {
    JPAQuery<Review> query = queryFactory.selectFrom(review)
        .leftJoin(review.reviewLikes, reviewLike)
        .fetchJoin()
        .where(
            eqStoreId(storeId),
            isDeleted(false)
        )
        .distinct();

    if (filter == null || filter == Filter.RECENT) {
      query.orderBy(review.createdAt.desc());
    } else if (filter == Filter.RECOMMEND) {
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
