package ssammudan.cotree.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    //Member: 0001 ~ 1000

    //${}: 1001 ~ 2000

    //${}: 2001 ~ 3000

    //${}: 3001 ~ 4000

    //${}: 4001 ~ 5000

    //${}: 5001 ~ 6000

    //${}: 6001 ~ 7000

    //${}: 7001 ~ 8000

    //${}: 8001 ~ 9000

    //Common: 9001 ~ 9999
    ;

    private final HttpStatus status;
    private final String     code;
    private final String     message;

}
