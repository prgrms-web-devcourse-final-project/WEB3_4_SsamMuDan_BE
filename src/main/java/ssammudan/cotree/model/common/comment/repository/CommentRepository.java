package ssammudan.cotree.model.common.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
