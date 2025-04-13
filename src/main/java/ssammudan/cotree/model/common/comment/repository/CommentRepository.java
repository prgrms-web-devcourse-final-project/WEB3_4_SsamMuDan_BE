package ssammudan.cotree.model.common.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssammudan.cotree.model.common.comment.entity.Comment;

/**
 * PackageName : ssammudan.cotree.model.common.comment.repository
 * FileName    : CommentRepository
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

	@Modifying
	@Query("DELETE FROM Comment c WHERE c.parentComment.id IS NOT NULL AND c.community.id = :communityId")
	void deleteChildComments(@Param("communityId") Long communityId);

	@Modifying
	@Query("DELETE FROM Comment c WHERE c.parentComment.id IS NULL AND c.community.id = :communityId")
	void deleteParentComments(@Param("communityId") Long communityId);
}
