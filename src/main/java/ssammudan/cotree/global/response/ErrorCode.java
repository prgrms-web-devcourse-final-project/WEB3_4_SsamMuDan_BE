package ssammudan.cotree.global.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	//Member: 0001 ~ 1000
	MEMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "0001", "이미 존재하는 회원입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "0002", "회원을 찾을 수 없습니다."),
	MEMBER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "0003", "인증정보가 일치하지 않습니다."),

	//Education: 1001 ~ 2000
	EDUCATION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "학습 카테고리를 찾을 수 없습니다."),
	EDUCATION_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "학습 난이도를 찾을 수 없습니다."),
	TECH_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "1003", "TechBook을 찾을 수 없습니다."),

	//Payment: 2001 ~ 3000

	//Recruitment: 3001 ~ 4000
	NOT_FOUND_RESUME(HttpStatus.NOT_FOUND, "3001", "해당하는 이력서를 찾을 수 없습니다"),

	//Project: 4001 ~ 5000
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "4001", "해당하는 프로젝트를 찾을 수 없습니다."),
	
	//Community: 5001 ~ 6000
	COMMUNITY_BOARD_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "5001", "유효하지 않은 글 카테고리 입니다."),
	COMMUNITY_MEMBER_NOTFOUND(HttpStatus.BAD_REQUEST, "5002", "존재하지 않은 회원입니다."),
	COMMUNITY_BOARD_NOTFOUND(HttpStatus.BAD_REQUEST, "5003", "존재하지 않은 글입니다."),

	//S3: 6001 ~ 7000
	FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "6001", "파일 업로드 실패, 재시도 혹은 관리자 문의해주세요."),

	//Comment: 7001 ~ 8000
	POST_COMMENT_FAIL_COMMUNITY_NOTFOUND(HttpStatus.BAD_REQUEST, "7001", "커뮤니티 댓글 작성 실패. 잘못된 글 ID 입니다."),
	POST_COMMENT_FAIL_RESUME_NOTFOUND(HttpStatus.BAD_REQUEST, "7002", "커뮤니티 댓글 작성 실패. 잘못된 글 ID 입니다."),
	POST_COMMENT_FAIL_PARENT_COMMENT_NOTFOUND(HttpStatus.BAD_REQUEST, "7003", "대댓글 작성 실패. 잘못된 댓글 ID 입니다."),
	POST_COMMENT_FAIL_INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "7004", "댓글 작성 실패. 잘못된 카테고리 입니다."),
	COMMENT_GET_FAIL_INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "7005", "댓글 조회 실패. 잘못된 카테고리 입니다."),

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	NOT_FOUND_URL(HttpStatus.NOT_FOUND, "9001", "요청하신 URL 을 찾을 수 없습니다."),
	NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "9002", "요청 메서드를 찾을 수 없습니다."),
	VALIDATION_FAIL_ERROR(HttpStatus.BAD_REQUEST, "9003", ""),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "9004", "올바르지 않은 입력값입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "9005", "올바르지 않은 HTTP 메서드입니다."),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "9006", "값을 찾지 못했습니다."),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "9007", "요청이 거부되었습니다."),
	METHOD_ARGUMENT_TYPE_MISS_MATCH(HttpStatus.BAD_REQUEST, "9008", "요청 파라미터 타입 불일치. API 문서 확인해주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 내부 오류 발생했습니다");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
