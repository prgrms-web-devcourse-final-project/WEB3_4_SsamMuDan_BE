package ssammudan.cotree.global.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	//Member: 0001 ~ 1000
	DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, "0001", "이미 존재하는 회원입니다."),
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "0002", "회원을 찾을 수 없습니다."),

	//Education: 1001 ~ 2000

	//Payment: 2001 ~ 3000

	//Recruitment: 3001 ~ 4000

	//Project: 4001 ~ 5000

	//Community: 5001 ~ 6000
	COMMUNITY_BOARD_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "5001", "유효하지 않은 글 카테고리 입니다."),
	COMMUNITY_MEMBER_NOTFOUND(HttpStatus.BAD_REQUEST, "5002", "존재하지 않은 회원입니다."),

	//${}: 6001 ~ 7000

	//${}: 7001 ~ 8000

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	NOT_FOUND_URL(HttpStatus.NOT_FOUND, "9001", "요청하신 URL 을 찾을 수 없습니다."),
	NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "9002", "요청 메서드를 찾을 수 없습니다."),
	VALIDATION_FAIL_ERROR(HttpStatus.BAD_REQUEST, "9003", ""),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "9004", "올바르지 않은 입력값입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "9005", "올바르지 않은 HTTP 메서드입니다."),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "9006", "값을 찾지 못했습니다."),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "9007", "요청이 거부되었습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 내부 오류 발생했습니다");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
