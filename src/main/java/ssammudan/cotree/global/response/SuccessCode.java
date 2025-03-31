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

	//Education
	TECH_BOOK_READ_SUCCESS(HttpStatus.OK, "200", "TechBook 조회를 완료했습니다."),

	//Payment

	//Recruitment

	//Project

	//Community
	COMMUNITY_BOARD_CREATE_SUCCESS(HttpStatus.CREATED, "201", "글 작성 성공"),

	//${}: 6001 ~ 7000

	//${}: 7001 ~ 8000

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}
