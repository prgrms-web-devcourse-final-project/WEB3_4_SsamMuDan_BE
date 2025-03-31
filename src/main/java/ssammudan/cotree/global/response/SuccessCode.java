package ssammudan.cotree.global.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

	//Member

	//Education
	TECH_BOOK_READ_SUCCESS(HttpStatus.OK, "200", "TechBook을 성공적으로 불러왔습니다."),

	//Payment

	//Recruitment

	//Project

	//Community

	//${}: 6001 ~ 7000

	//${}: 7001 ~ 8000

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}
