package ssammudan.cotree.global.config.security.oauth.dto;

/**
 * PackageName : ssammudan.cotree.global.config.security.oauth
 * FileName    : OAuth2Response
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
public interface OAuth2Response {
	//제공자 (Ex. naver, google, ...)
	String getProvider();

	//제공자에서 발급해주는 아이디(번호)
	String getProviderId();

	//이메일
	String getEmail();

	//사용자 실명 (설정한 이름)
	String getName();

	// //전화번호
	// String getPhoneNumber();
}
