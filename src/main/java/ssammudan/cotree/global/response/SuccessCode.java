package ssammudan.cotree.global.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

	//Member
	MEMBER_CREATE_SUCCESS(HttpStatus.CREATED, "201", "회원가입을 완료했습니다."),
	MEMBER_SIGNIN_SUCCESS(HttpStatus.OK, "200", "로그인을 완료했습니다."),
	MEMBER_SIGNOUT_SUCCESS(HttpStatus.OK, "200", "로그아웃을 완료했습니다."),
	MEMBER_INFO_REQUEST_SUCCESS(HttpStatus.OK, "200", "회원정보 조회를 완료했습니다."),
	MEMBER_INFO_UPDATE_SUCCESS(HttpStatus.OK, "200", "회원정보 수정을 완료했습니다."),
	MEMBER_SIGNUP_CODE_SEND_SUCCESS(HttpStatus.OK, "200", "회원가입 인증번호 발송 성공"),
	MEMBER_SIGNUP_CODE_VERIFY_SUCCESS(HttpStatus.OK, "200", "회원가입 전화번호 인증 성공"),
	MEMBER_RECOVER_CODE_SEND_SUCCESS(HttpStatus.OK, "200", "아이디 찾기 인증번호 발송 성공"),
	MEMBER_RECOVER_CODE_VERIFY_SUCCESS(HttpStatus.OK, "200", "아이디 찾기 전화번호 인증 성공"),
	MEMBER_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "200", "비밀번호 변경 성공"),

	//Education
	TECH_BOOK_READ_SUCCESS(HttpStatus.OK, "200", "TechBook 조회를 성공했습니다."),
	TECH_BOOK_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "TechBook 목록 조회를 성공했습니다."),
	TECH_TUBE_READ_SUCCESS(HttpStatus.OK, "200", "TechTube 조회를 성공했습니다."),
	TECH_TUBE_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "TechTube 목록 조회를 성공했습니다."),
	TECH_EDUCATION_REVIEW_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "리뷰 목록 조회를 성공했습니다."),
	TECH_EDUCATION_REVIEW_CREATE_SUCCESS(HttpStatus.CREATED, "201", "리뷰 등록을 성공했습니다."),

	//Payment
	PRE_PAYMENT_SAVE_SUCCESS(HttpStatus.OK, "200", "사전 결제 정보 저장을 완료했습니다."),
	TOSS_PAYMENT_SUCCESS(HttpStatus.OK, "200", "토스 결제가 승인되었습니다."),

	//Recruitment
	RESUME_CREATE_SUCCESS(HttpStatus.CREATED, "201", "이력서 작성을 성공하였습니다."),
	RESUME_DETAIL_SUCCESS(HttpStatus.OK, "200", "이력서 상세 조회를 성공하였습니다."),
	RESUME_LIST_SUCCESS(HttpStatus.OK, "200", "이력서 리스트 조회를 성공하였습니다."),

	//Project
	PROJECT_CREATE_SUCCESS(HttpStatus.CREATED, "201", "프로젝트 생성 성공"),
	PROJECT_HOT_LIST_SEARCH_SUCCESS(HttpStatus.OK, "200", "Hot 프로젝트 목록 조회 성공"),
	PROJECT_LIST_SEARCH_SUCCESS(HttpStatus.OK, "200", "프로젝트 목록 조회 성공"),
	PROJECT_FETCH_SUCCESS(HttpStatus.OK, "200", "프로젝트 상세 조회 성공"),
	PROJECT_STATUS_UPDATE_SUCCESS(HttpStatus.OK, "200", "프로젝트 모집 상태 변경 성공"),
	PROJECT_APPLY_SUCCESS(HttpStatus.OK, "200", "프로젝트 참가 신청 성공"),
	PROJECT_MEMBERSHIP_LIST_RETRIEVED(HttpStatus.OK, "200", "프로젝트 참가자 목록 조회 성공"),

	//Community
	COMMUNITY_BOARD_CREATE_SUCCESS(HttpStatus.CREATED, "201", "글 작성 성공"),
	COMMUNITY_BOARD_SEARCH_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 조회 성공"),
	COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 상세 조회 성공"),
	COMMUNITY_BOARD_MODIFY_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 수정 성공"),
	COMMUNITY_BOARD_DELETE_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 삭제 성공"),

	//S3 Upload
	S3_FILE_UPLOAD_SUCCESS(HttpStatus.CREATED, "201", "파일 업로드 성공."),

	//Comment:
	COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "201", "댓글 작성 성공"),
	COMMENT_SEARCH_SUCCESS(HttpStatus.OK, "200", "댓글 목록 조회 성공"),

	//Like
	LIKE_ADD_SUCCESS(HttpStatus.OK, "200", "좋아요 추가 성공"),

	// email
	EMAIL_CODE_SEND_SUCCESS(HttpStatus.OK, "200", "이메일 인증 코드 발송 성공"),
	EMAIL_CODE_VERIFY_SUCCESS(HttpStatus.OK, "200", "이메일 인증 코드 확인 성공"),

	//Common: 9001 ~ 9999
	TECH_STACK_FIND_SUCCESS(HttpStatus.OK, "200", "기술 스택 조회 성공"),
	EDUCATION_CATEGORY_FIND_SUCCESS(HttpStatus.OK, "200", "교육 카테고리 조회 성공"),
	COMMUNITY_CATEGORY_FIND_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 카테고리 조회 성공"),
	POSITION_FIND_SUCCESS(HttpStatus.OK, "200", "개발 직무 조회 성공"),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}
