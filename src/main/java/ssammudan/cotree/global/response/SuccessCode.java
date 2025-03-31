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

	//Payment

	//Recruitment

	//Project

	//Community

	//S3 Upload
	S3_FILE_UPLOAD_SUCCESS(HttpStatus.CREATED, "201", "파일 업로드 성공."),

	//${}: 7001 ~ 8000

	//${}: 8001 ~ 9000

	//Common: 9001 ~ 9999
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}
