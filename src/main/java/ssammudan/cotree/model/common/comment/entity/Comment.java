package ssammudan.cotree.model.common.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;

/**
 * PackageName : ssammudan.cotree.model.common.comment.entity
 * FileName    : Comment
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parentComment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "community_id")
	private Community community;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resume_id")
	private Resume resume;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Builder(access = AccessLevel.PRIVATE)
	private Comment(Comment parentComment, Member author, Community community, Resume resume, String content) {
		this.parentComment = parentComment;
		this.author = author;
		this.community = community;
		this.resume = resume;
		this.content = content;
	}

	public static Comment createNewCommunityComment(Member author, Community community, Comment parrentComment,
			String content) {
		return Comment
				.builder()
				.parentComment(parrentComment)
				.author(author)
				.community(community)
				.resume(null)
				.content(content)
				.build();
	}

	public static Comment createNewResumeComment(Member author, Resume resume, Comment parrentComment, String content) {
		return Comment
				.builder()
				.parentComment(parrentComment)
				.author(author)
				.community(null)
				.resume(resume)
				.content(content)
				.build();
	}
}
