package ssammudan.cotree.model.community.community.entity;

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
import ssammudan.cotree.model.community.category.entity.CommunityCategory;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.community.community.entity
 * FileName    : Community
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : community table `JPA Entity`
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@Getter
@Entity
@Table(name = "community")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "community_category_id", nullable = false)
	private CommunityCategory communityCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;

	@Column(name = "thumbnail_image", columnDefinition = "TEXT")
	private String thumbnailImage;

	@Builder(access = AccessLevel.PRIVATE)
	private Community(CommunityCategory communityCategory, Member member, String title, String content,
		Integer viewCount, String thumbnailImage) {
		this.communityCategory = communityCategory;
		this.member = member;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.thumbnailImage = thumbnailImage;
	}

	public static Community createNewCommunityBoard(
		CommunityCategory communityCategory,
		Member member,
		String title,
		String content,
		String thumbnailImage
	) {
		return Community
			.builder()
			.communityCategory(communityCategory)
			.member(member)
			.title(title)
			.content(content)
			.viewCount(0)
			.thumbnailImage(thumbnailImage)
			.build();
	}

	public void modifyCommunity(final String title, final String content) {
		this.title = title;
		this.content = content;
	}
}
