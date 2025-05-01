package ssammudan.cotree.model.education.techbook.techbook.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techbook.dto.TechBookSimpleInfoDto;
import ssammudan.cotree.model.education.techbook.techbook.entity.QTechBook;

@Repository
@RequiredArgsConstructor
public class TechBookQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<TechBookSimpleInfoDto> findSimpleInfoById(Long id) {
		QTechBook techBook = QTechBook.techBook;

		return Optional.ofNullable(
			queryFactory
				.select(Projections.constructor(
					TechBookSimpleInfoDto.class,
					techBook.id,
					techBook.totalRating,
					techBook.totalReviewCount
				))
				.from(techBook)
				.where(techBook.id.eq(id))
				.fetchOne()
		);
	}
}
