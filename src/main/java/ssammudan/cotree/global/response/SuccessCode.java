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

	//Education
	TECH_BOOK_READ_SUCCESS(HttpStatus.OK, "200", "TechBook 조회를 완료했습니다."),
	TECH_BOOK_LIST_FIND_SUCCESS(HttpStatus.OK, "200", "TechBook 목록 조회를 완료했습니다."),

	//Payment

	//Recruitment
	RESUME_CREATE_SUCCESS(HttpStatus.CREATED, "201", "이력서 작성을 성공하였습니다."),
	RESUME_DETAIL_SUCCESS(HttpStatus.OK, "200", "이력서 상세 조회를 성공하였습니다."),

	//Project
	PROJECT_CREATE_SUCCESS(HttpStatus.CREATED, "201", "프로젝트 생성 성공"),

	//Community
	COMMUNITY_BOARD_CREATE_SUCCESS(HttpStatus.CREATED, "201", "글 작성 성공"),
	COMMUNITY_BOARD_SEARCH_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 조회 성공"),
	COMMUNITY_BOARD_DETAIL_SEARCH_SUCCESS(HttpStatus.OK, "200", "커뮤니티 글 상세 조회 성공"),

	//S3 Upload
	S3_FILE_UPLOAD_SUCCESS(HttpStatus.CREATED, "201", "파일 업로드 성공."),

	//Comment:
	COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "201", "댓글 작성 성공"),

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	TECH_STACK_FIND_SUCCESS(HttpStatus.OK, "200", "기술 스택 조회 성공"),
	POSITION_FIND_SUCCESS(HttpStatus.OK, "200", "개발 직무 조회 성공");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
