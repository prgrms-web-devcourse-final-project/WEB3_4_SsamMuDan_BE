package ssammudan.cotree.model.education.techtube.techtube.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.education.techtube.dto.TechTubeSimpleInfoDto;
import ssammudan.cotree.model.education.techtube.techtube.entity.QTechTube;

@Repository
@RequiredArgsConstructor
public class TechTubeQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<TechTubeSimpleInfoDto> findSimpleInfoById(Long id) {
		QTechTube techTube = QTechTube.techTube;

		return Optional.ofNullable(
			queryFactory
				.select(Projections.constructor(
					TechTubeSimpleInfoDto.class,
					techTube.id,
					techTube.totalRating,
					techTube.totalReviewCount
				))
				.from(techTube)
				.where(techTube.id.eq(id))
				.fetchOne()
		);
	}
}
