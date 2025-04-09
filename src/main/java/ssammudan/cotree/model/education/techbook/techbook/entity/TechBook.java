package ssammudan.cotree.model.education.techbook.techbook.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.common.like.entity.Like;
import ssammudan.cotree.model.education.level.entity.EducationLevel;
import ssammudan.cotree.model.education.techbook.category.entity.TechBookEducationCategory;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.education.techbook.techbook.entity
 * FileName    : TechBook
 * Author      : loadingKKamo21
 * Date        : 25. 3. 28.
 * Description : TechBook 엔티티
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.    loadingKKamo21       Initial creation
 * 25. 4. 9.     Baekgwa                 ViewCount 증가 `ViewCountStore` 에서 통합 관리 진행
 */
@Entity
@Table(name = "tech_book")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechBook extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "member_id",
		referencedColumnName = "id",
		nullable = false,
		updatable = false,
		foreignKey = @ForeignKey(name = "fk_tech_book_member_id")
	)
	private Member writer;    //TechBook 저자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "education_level_id",
		referencedColumnName = "id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_tech_book_education_level_id")
	)
	private EducationLevel educationLevel;    //TechBook 학습 난이도

	@Column(name = "title", nullable = false)
	private String title;    //TechBook 제목

	@Column(name = "description", nullable = false)
	private String description;    //TechBook 설명

	@Column(name = "introduction", columnDefinition = "TEXT", nullable = false)
	private String introduction;    //TechBook 소개

	@Column(name = "total_rating", nullable = false)
	@Builder.Default
	private Integer totalRating = 0;    //TechBook 전체 누적 평점

	@Column(name = "total_review_count", nullable = false)
	@Builder.Default
	private Integer totalReviewCount = 0;    //TechBook 전체 리뷰 수

	@Column(name = "tech_book_url", nullable = false)
	private String techBookUrl;    //TechBook PDF URL

	@Column(name = "tech_book_preview_url", nullable = false)
	private String techBookPreviewUrl;    //TechBook PDF 프리뷰 URL

	@Column(name = "tech_book_thumbnail_url", nullable = false)
	private String techBookThumbnailUrl;    //TechBook 썸네일 URL

	@Column(name = "tech_book_page", nullable = false)
	private Integer techBookPage;    //TechBook 페이지 수

	@Column(name = "price", nullable = false)
	private Integer price;    //TechBook 가격

	@Column(name = "view_count", nullable = false)
	@Builder.Default
	private Integer viewCount = 0; //TechBook 조회 수

	@OneToMany(mappedBy = "techBook")
	@Builder.Default
	private List<TechBookEducationCategory> techBookEducationCategories = new ArrayList<>();

	@OneToMany(mappedBy = "techBook")
	@Builder.Default
	private List<Like> likes = new ArrayList<>();    //TechBook 좋아요

	public static TechBook create(
		final Member writer,
		final EducationLevel educationLevel,
		final String title,
		final String description,
		final String introduction,
		final String techBookUrl,
		final String techBookPreviewUrl,
		final String techBookThumbnailUrl,
		final Integer techBookPage,
		final Integer price
	) {
		//TODO: TechBook 저자 연관관계 설정 확인 필요
		TechBook techBook = TechBook.builder()
			.writer(writer)
			.title(title)
			.description(description)
			.introduction(introduction)
			.techBookUrl(techBookUrl)
			.techBookPreviewUrl(techBookPreviewUrl)
			.techBookThumbnailUrl(techBookThumbnailUrl)
			.techBookPage(techBookPage)
			.price(price)
			.build();
		techBook.setRelationshipWithEducationLevel(educationLevel);
		return techBook;
	}

	@PrePersist
	private void prePersist() {
		if (this.totalRating == null) {
			this.totalRating = 0;
		}

		if (this.totalReviewCount == null) {
			this.totalReviewCount = 0;
		}

		if (this.viewCount == null) {
			this.viewCount = 0;
		}

		if (this.techBookEducationCategories == null) {
			this.techBookEducationCategories = new ArrayList<>();
		}
	}

	private void setRelationshipWithEducationLevel(final EducationLevel educationLevel) {
		this.educationLevel = educationLevel;
		educationLevel.getTechBooks().add(this);
	}

	/**
	 * TechBook 엔티티 학습 난이도(EducationLevel) 수정
	 *
	 * @param newEducationLevel - 새로운 TechBook 학습 난이도
	 * @return this
	 */
	public TechBook modifyEducationLevel(final EducationLevel newEducationLevel) {
		if (!this.educationLevel.getId().equals(newEducationLevel.getId())) {
			this.educationLevel.getTechBooks().remove(this);
			setRelationshipWithEducationLevel(newEducationLevel);
		}
		return this;
	}

	/**
	 * TechBook 엔티티 정보 수정
	 *
	 * @param newTitle - 새로운 TechBook 제목
	 * @param newDescription - 새로운 TechBook 설명
	 * @param newIntroduction - 새로운 TechBook 소개
	 * @param newTechBookUrl - 새로운 TechBook PDF URL
	 * @param newTechBookPreviewUrl - 새로운 TechBook PDF 프리뷰 URL
	 * @param newTechBookThumbnailUrl - 새로운 TechBook 썸네일 URL
	 * @param newTechBookPage - 새로운 TechBook 페이지 수
	 * @param newPrice - 새로운 TechBook 가격
	 * @return this
	 */
	public TechBook modifyDetail(
		final String newTitle,
		final String newDescription,
		final String newIntroduction,
		final String newTechBookUrl,
		final String newTechBookPreviewUrl,
		final String newTechBookThumbnailUrl,
		final Integer newTechBookPage,
		final Integer newPrice
	) {
		if (!this.title.equals(newTitle) && !newTitle.isBlank()) {
			this.title = newTitle;
		}

		if (!this.description.equals(newDescription) && !newDescription.isBlank()) {
			this.description = newDescription;
		}

		if (!this.introduction.equals(newIntroduction) && !newIntroduction.isBlank()) {
			this.introduction = newIntroduction;
		}

		if (!this.techBookUrl.equals(newTechBookUrl) && !newTechBookUrl.isBlank()) {
			this.techBookUrl = newTechBookUrl;
		}

		if (!this.techBookPreviewUrl.equals(newTechBookPreviewUrl) && !newTechBookPreviewUrl.isBlank()) {
			this.techBookPreviewUrl = newTechBookPreviewUrl;
		}

		if (!this.techBookThumbnailUrl.equals(newTechBookThumbnailUrl) && !newTechBookThumbnailUrl.isBlank()) {
			this.techBookThumbnailUrl = newTechBookThumbnailUrl;
		}

		if (!this.techBookPage.equals(newTechBookPage) && newTechBookPage != null) {
			this.techBookPage = newTechBookPage;
		}

		if (!this.price.equals(newPrice) && newPrice != null) {
			this.price = newPrice;
		}

		return this;
	}

	/**
	 * TechBook 조회 수 증가
	 *
	 * @return this
	 */
	@Deprecated(since = "2025-04-09", forRemoval = true)
	public TechBook increseViewCount() {
		this.viewCount += 1;
		return this;
	}

	/**
	 * TechBook 누적 리뷰 점수 추가
	 *
	 * @param rating - 리뷰 점수
	 * @return this
	 */
	public TechBook addReviewRating(final Integer rating) {
		this.totalRating += rating;
		this.totalReviewCount += 1;
		return this;
	}

}
