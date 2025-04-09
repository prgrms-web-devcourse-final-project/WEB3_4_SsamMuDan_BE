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
	MEMBER_SIGNUP_VERIFY_FAILED(HttpStatus.UNAUTHORIZED, "0004", "회원가입 인증번호가 일치하지 않습니다."),
	MEMBER_SIGNUP_COOLDOWN(HttpStatus.BAD_REQUEST, "0005", "너무 많이 시도하였습니다. 잠시 후 재시도 해주세요"),
	MEMBER_RECOVER_COOLDOWN(HttpStatus.BAD_REQUEST, "0006", "너무 많이 시도하였습니다. 잠시 후 재시도 해주세요"),
	MEMBER_NAME_NOT_MATCH_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "0007", "이름과 전화번호가 일치하지 않습니다."),

	//Education: 1001 ~ 2000
	EDUCATION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "학습 카테고리를 찾을 수 없습니다."),
	EDUCATION_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "학습 난이도를 찾을 수 없습니다."),
	TECH_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "1003", "TechBook을 찾을 수 없습니다."),
	TECH_TUBE_NOT_FOUND(HttpStatus.NOT_FOUND, "1004", "TechTube을 찾을 수 없습니다."),
	TECH_EDUCATION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "1005", "TechEducation 카테고리를 찾을 수 없습니다."),
	TECH_EDUCATION_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "1006", "TechEducation 리뷰를 찾을 수 없습니다."),
	TECH_EDUCATION_REVIEW_DUPLICATED(HttpStatus.BAD_REQUEST, "1007", "작성된 리뷰가 존재합니다."),
	INVALID_EDUCATION_CATEGORY_ID(HttpStatus.BAD_REQUEST, "1008", "유효하지 않은 Education Category 입니다."),

	//Payment: 2001 ~ 3000
	PAYMENT_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "2001", "결제는 완료되었으나, 서버 처리에 실패했습니다."),
	PAYMENT_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "2002", "결제 검증에 실패했습니다."),
	PAYMENT_ALREADY_PROCESSED(HttpStatus.CONFLICT, "2003", "이미 처리된 결제입니다."),
	PAYMENT_EXPIRED_PREPAYMENT(HttpStatus.BAD_REQUEST, "2004", "결제 대기 시간이 만료되었습니다."),
	PAYMENT_REQUEST_INVALID(HttpStatus.BAD_REQUEST, "2005", "결제 정보가 유효하지 않습니다."),
	ORDER_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "2006", "제품 카테고리를 찾을 수 없습니다."),
	TOSS_API_ERROR(HttpStatus.BAD_GATEWAY, "2007", "토스 결제 서버에서 오류가 발생했습니다."),
	TOSS_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "2008", "토스 결제 서버의 응답이 지연되고 있습니다."),

	//Recruitment: 3001 ~ 4000
	NOT_FOUND_RESUME(HttpStatus.NOT_FOUND, "3001", "해당하는 이력서를 찾을 수 없습니다"),
	NOR_FOUND_RESUME_ID(HttpStatus.NOT_FOUND, "3001", "해당하는 이력서 ID 를 가져올 수 없습니다."),

	//Project: 4001 ~ 5000
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "4001", "해당하는 프로젝트를 찾을 수 없습니다."),
	PROJECT_OWNER_ONLY(HttpStatus.FORBIDDEN, "4002", "해당 프로젝트의 생성자만 수정할 수 있습니다."),
	PROJECT_OWNER_CANNOT_JOIN(HttpStatus.FORBIDDEN, "4003", "프로젝트 생성자는 참가 신청할 수 없습니다."),
	PROJECT_NOT_OPEN(HttpStatus.FORBIDDEN, "4004", "프로젝트가 모집중이 아닙니다."),
	PROJECT_MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "4005", "이미 프로젝트에 참가 신청한 기록이 있습니다."),

	//Community: 5001 ~ 6000
	COMMUNITY_BOARD_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "5001", "유효하지 않은 글 카테고리 입니다."),
	COMMUNITY_MEMBER_NOTFOUND(HttpStatus.NOT_FOUND, "5002", "존재하지 않은 회원입니다."),
	COMMUNITY_BOARD_NOTFOUND(HttpStatus.NOT_FOUND, "5003", "존재하지 않은 글입니다."),
	COMMUNITY_BOARD_OPERATION_FAIL_NOT_AUTHOR(HttpStatus.FORBIDDEN, "5004", "작성자만 수정할 수 있습니다."),

	//S3: 6001 ~ 7000
	FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "6001", "파일 업로드 실패, 재시도 혹은 관리자 문의해주세요."),
	INVALID_FILE(HttpStatus.BAD_REQUEST, "6002", "파일 업로드 실패. 잘못된 파일 입니다."),

	//Comment: 7001 ~ 8000
	POST_COMMENT_FAIL_COMMUNITY_NOTFOUND(HttpStatus.NOT_FOUND, "7001", "커뮤니티 댓글 작성 실패. 잘못된 글 ID 입니다."),
	POST_COMMENT_FAIL_RESUME_NOTFOUND(HttpStatus.NOT_FOUND, "7002", "커뮤니티 댓글 작성 실패. 잘못된 글 ID 입니다."),
	POST_COMMENT_FAIL_PARENT_COMMENT_NOTFOUND(HttpStatus.NOT_FOUND, "7003", "대댓글 작성 실패. 잘못된 댓글 ID 입니다."),
	POST_COMMENT_FAIL_INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "7004", "댓글 작성 실패. 잘못된 카테고리 입니다."),
	COMMENT_GET_FAIL_INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "7005", "댓글 조회 실패. 잘못된 카테고리 입니다."),

	//Like: 8001 ~ 9000
	LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "8001", "좋아요 정보를 찾을 수 없습니다."),
	LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "8002", "이미 좋아요한 컨텐츠입니다."),

	//Common: 9001 ~ 9999
	NOT_FOUND_URL(HttpStatus.NOT_FOUND, "9001", "요청하신 URL 을 찾을 수 없습니다."),
	NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "9002", "요청 메서드를 찾을 수 없습니다."),
	VALIDATION_FAIL_ERROR(HttpStatus.BAD_REQUEST, "9003", ""),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "9004", "올바르지 않은 입력값입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "9005", "올바르지 않은 HTTP 메서드입니다."),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "9006", "값을 찾지 못했습니다."),
	HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "9007", "요청이 거부되었습니다."),
	METHOD_ARGUMENT_TYPE_MISS_MATCH(HttpStatus.BAD_REQUEST, "9008", "요청 파라미터 타입 불일치. API 문서 확인해주세요."),
	SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "9009", "SMS 가 전송 실패하였습니다."),
	EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "9010", "이메일 전송에 실패했습니다."),
	EMAIL_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "9011", "이메일 인증번호가 유효하지 않습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 내부 오류 발생했습니다");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
