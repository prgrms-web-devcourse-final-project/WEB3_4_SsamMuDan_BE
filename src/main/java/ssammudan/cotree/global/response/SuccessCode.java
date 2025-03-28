package ssammudan.cotree.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    //Member

    //Education

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
    private final String     code;
    private final String     message;

}
