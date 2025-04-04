package ssammudan.cotree.model.recruitment.resume.resume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssammudan.cotree.domain.resume.dto.query.TechStackInfo;
import ssammudan.cotree.domain.resume.dto.query.BasicInfoQueryDto;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.recruitment.resume.resume.repository
 * FileName    : ResumeRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryJdbc, ResumeRepositoryQueryDsl {

	@Query("""
		SELECT new ssammudan.cotree.domain.resume.dto.query.BasicInfoQueryDto
		(m.username, r.profileImage, r.years, r.email, r.introduction, r.viewCount)
		FROM Resume r JOIN r.member m
		WHERE r.id = :resumeId
		""")
	Optional<BasicInfoQueryDto> findBasicInfoQueryDto(@Param("resumeId") Long resumeId);

	@Query("""
		SELECT dp.name
		FROM ResumeDevelopmentPosition rdp JOIN rdp.developmentPosition dp
		WHERE rdp.resume.id = :resumeId
		""")
	List<String> findDevelopmentPositionNames(@Param("resumeId") Long resumeId);

	@Query("""
		SELECT new ssammudan.cotree.domain.resume.dto.query.TechStackInfo
		(ts.name, ts.imageUrl)
		FROM ResumeTechStack rts JOIN rts.techStack ts
		WHERE rts.resume.id = :resumeId
		""")
	List<TechStackInfo> findTechStackInfos(@Param("resumeId") Long resumeId);
}
