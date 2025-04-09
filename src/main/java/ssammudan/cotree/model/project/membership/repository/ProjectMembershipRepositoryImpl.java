package ssammudan.cotree.model.project.membership.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.project.membership.dto.MembershipResponse;
import ssammudan.cotree.domain.project.membership.dto.QMembershipResponse;
import ssammudan.cotree.model.member.member.entity.QMember;
import ssammudan.cotree.model.project.membership.entity.QProjectMembership;
import ssammudan.cotree.model.project.membership.type.ProjectMembershipStatus;

/**
 * PackageName : ssammudan.cotree.model.project.membership.repository
 * FileName    : ProjectMembershipRepositoryImpl
 * Author      : sangxxjin
 * Date        : 2025. 4. 9.
 * Description : ProjectMembershipRepositoryImpl
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 9.     sangxxjin               Initial creation
 */
@Repository
@RequiredArgsConstructor
public class ProjectMembershipRepositoryImpl implements ProjectMembershipRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MembershipResponse> findMembershipResponsesByProjectId(Long projectId) {
		QProjectMembership pm = QProjectMembership.projectMembership;
		QMember m = QMember.member;

		return queryFactory
			.select(new QMembershipResponse(
				pm.id,
				pm.projectMembershipStatus,
				m.username,
				m.nickname,
				pm.createdAt,
				ExpressionUtils.as(
					new CaseBuilder()
						.when(pm.projectMembershipStatus.ne(ProjectMembershipStatus.PENDING))
						.then(pm.modifiedAt)
						.otherwise((LocalDateTime)null),
					"respondedAt"
				)
			))
			.from(pm)
			.join(pm.member, m)
			.where(pm.project.id.eq(projectId))
			.fetch();
	}

}
