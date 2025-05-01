package ssammudan.cotree.global.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.global.response
 * FileName    : PageResponse
 * Author      : Baekgwa
 * Date        : 2025-04-01
 * Description : PageResponse 공통 응답 객체. of static method 로 Page 객체를 쉽게 변경 가능
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-01     Baekgwa               Initial creation
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {
	private final List<T> content;
	private final int pageNo;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;
	private final boolean isLast;
	private final boolean isFirst;
	private final boolean hasNext;
	private final boolean hasPrevious;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final Double reviewAvgRating;

	public static <T> PageResponse<T> of(Page<T> page) {
		return PageResponse
			.<T>builder()
			.content(page.getContent())
			.pageNo(page.getNumber())
			.pageSize(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.isLast(page.isLast())
			.isFirst(page.isFirst())
			.hasNext(page.hasNext())
			.hasPrevious(page.hasPrevious())
			.build();
	}

	public static <T> PageResponse<T> from(Page<T> page, Double reviewAvgRating) {
		return PageResponse
			.<T>builder()
			.content(page.getContent())
			.pageNo(page.getNumber())
			.pageSize(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.isLast(page.isLast())
			.isFirst(page.isFirst())
			.hasNext(page.hasNext())
			.hasPrevious(page.hasPrevious())
			.reviewAvgRating(reviewAvgRating)
			.build();
	}

	public static <T> PageResponse<T> from(List<T> content, Pageable pageable, long totalElements,
		Double reviewAvgRating) {
		int pageSize = pageable.getPageSize();
		int pageNo = pageable.getPageNumber();
		boolean isLast = (pageable.getOffset() + content.size()) >= totalElements;
		boolean isFirst = pageNo == 0;
		boolean hasNext = !isLast;
		boolean hasPrevious = pageNo > 0;
		int totalPages = (int)Math.ceil((double)totalElements / pageSize);

		return PageResponse
			.<T>builder()
			.content(content)
			.pageNo(pageNo)
			.pageSize(pageSize)
			.totalElements(totalElements)
			.totalPages(totalPages)
			.isLast(isLast)
			.isFirst(isFirst)
			.hasNext(hasNext)
			.hasPrevious(hasPrevious)
			.reviewAvgRating(reviewAvgRating)
			.build();
	}
}
