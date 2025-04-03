package ssammudan.cotree.model.common.like.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.community.community.entity.Community;
import ssammudan.cotree.model.education.techbook.techbook.entity.TechBook;
import ssammudan.cotree.model.education.techtube.techtube.entity.TechTube;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.project.project.entity.Project;

/**
 * PackageName : ssammudan.cotree.model.common.like.entity
 * FileName    : Like
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
@Table(name = "likes")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Like extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tech_tube_id", updatable = false)
	private TechTube techTube;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tech_book_id", updatable = false)
	private TechBook techBook;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", updatable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "community_id", updatable = false)
	private Community community;

	public static Like create(final Member member, final TechTube techTube) {
		Like like = Like.builder().member(member).build();
		like.setRelationshipWithTechTube(techTube);
		return like;
	}

	public static Like create(final Member member, final TechBook techBook) {
		Like like = Like.builder().member(member).build();
		like.setRelationshipWithTechBook(techBook);
		return like;
	}

	public static Like create(final Member member, final Project project) {
		return Like.builder().member(member).project(project).build();
	}

	public static Like create(final Member member, final Community community) {
		return Like.builder().member(member).community(community).build();
	}

	private void setRelationshipWithTechTube(final TechTube techTube) {
		this.techTube = techTube;
		techTube.getLikes().add(this);
	}

	private void setRelationshipWithTechBook(final TechBook techBook) {
		this.techBook = techBook;
		techBook.getLikes().add(this);
	}

}
