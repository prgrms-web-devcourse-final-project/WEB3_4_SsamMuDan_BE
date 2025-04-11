package ssammudan.cotree.model.common.like.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.community.dto.CommunityResponse;
import ssammudan.cotree.model.common.like.entity.QLike;
import ssammudan.cotree.model.community.community.entity.QCommunity;
import ssammudan.cotree.model.member.member.entity.QMember;

/**
 * PackageName : ssammudan.cotree.model.common.like.repository
 * FileName    : LikeRepositoryImpl
 * Author      : loadingKKamo21
 * Date        : 25. 4. 2.
 * Description : Like Querydsl 리포지토리 구현체
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     loadingKKamo21       Initial creation
 * 2025-04-11     Baekgwa               내가 좋아요 (관심)한, Community 목록 조회 기능 추가
 */
@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private static final QCommunity community = QCommunity.community;
	private static final QLike like = QLike.like;
	private static final QMember member = QMember.member;

	@Override
	public Page<CommunityResponse.BoardLikeListDetail> findBoardLikeList(
		Pageable pageable, String memberId) {

		List<CommunityResponse.BoardLikeListDetail> content = jpaQueryFactory
			.select(Projections.constructor(CommunityResponse.BoardLikeListDetail.class,
				community.id,
				community.title,
				member.nickname,
				community.createdAt,
				community.content,
				community.thumbnailImage
			))
			.from(like)
			.join(community).on(community.id.eq(like.community.id))
			.join(member).on(member.id.eq(like.member.id))
			.where(
				like.member.id.eq(memberId),
				like.community.id.isNotNull()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(like.createdAt.desc())
			.fetch();

		// Count 쿼리
		Long total = jpaQueryFactory
			.select(like.count())
			.from(like)
			.where(
				like.member.id.eq(memberId),
				like.community.id.isNotNull()
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0);
	}
}
