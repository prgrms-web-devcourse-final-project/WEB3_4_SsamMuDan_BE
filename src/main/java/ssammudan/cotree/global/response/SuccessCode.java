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

	//Payment

	//Recruitment
	RESUME_CREATE_SUCCESS(HttpStatus.CREATED, "201", "이력서 작성을 성공하였습니다.")

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
