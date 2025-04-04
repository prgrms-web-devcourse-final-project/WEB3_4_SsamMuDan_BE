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
	MEMBER_SIGNUP_CODE_SEND_SUCCESS(HttpStatus.OK, "200", "회원가입 전화번호 발송 성공"),
	MEMBER_SIGNUP_CODE_VERIFY_SUCCESS(HttpStatus.OK, "200", "회원가입 전화번호 인증 성공"),

	//Education
	TECH_BOOK_READ_SUCCESS(HttpStatus.OK, "200", "TechBook 조회를 성공했습니다."),
	TECH_BOOK_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "TechBook 목록 조회를 성공했습니다."),
	TECH_TUBE_READ_SUCCESS(HttpStatus.OK, "200", "TechTube 조회를 성공했습니다."),
	TECH_TUBE_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "TechTube 목록 조회를 성공했습니다."),
	TECH_EDUCATION_REVIEW_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "리뷰 목록 조회를 성공했습니다."),
	TECH_EDUCATION_REVIEW_CREATE_SUCCESS(HttpStatus.CREATED, "201", "리뷰 등록을 성공했습니다."),

	//Payment

	//Recruitment
	RESUME_CREATE_SUCCESS(HttpStatus.CREATED, "201", "이력서 작성을 성공하였습니다."),
	RESUME_DETAIL_SUCCESS(HttpStatus.OK, "200", "이력서 상세 조회를 성공하였습니다."),
	RESUME_LIST_SUCCESS(HttpStatus.OK, "200", "이력서 리스트 조회를 성공하였습니다."),

	//Project
	PROJECT_CREATE_SUCCESS(HttpStatus.CREATED, "201", "프로젝트 생성 성공"),
	PROJECT_HOT_LIST_SEARCH_SUCCESS(HttpStatus.OK, "200", "Hot 프로젝트 목록 조회 성공"),
	PROJECT_LIST_SEARCH_SUCCESS(HttpStatus.OK, "200", "프로젝트 목록 조회 성공"),
	PROJECT_FETCH_SUCCESS(HttpStatus.OK, "200", "프로젝트 상세 조회 성공"),

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

	//Common: 9001 ~ 9999
	TECH_STACK_FIND_SUCCESS(HttpStatus.OK, "200", "기술 스택 조회 성공"),
	POSITION_FIND_SUCCESS(HttpStatus.OK, "200", "개발 직무 조회 성공");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
