package ssammudan.cotree.domain.comment.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.domain.comment.type
 * FileName    : CommentCategory
 * Author      : Baekgwa
 * Date        : 2025-04-02
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-02     Baekgwa               Initial creation
 */
@Getter
@RequiredArgsConstructor
public enum CommentCategory {
	RESUME("이력서 댓글"),
	COMMUNITY("커뮤니티 글 댓글");

	private final String description;
}
