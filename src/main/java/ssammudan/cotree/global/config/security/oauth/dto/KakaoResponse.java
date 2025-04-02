package ssammudan.cotree.global.config.security.oauth.dto;

import java.util.Map;

/**
 * PackageName : ssammudan.cotree.global.config.security.oauth.dto
 * FileName    : KakaoResponse
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public KakaoResponse(Map<String, Object> attributes) {
		this.attribute = attributes; // 따로 확인 후 설정 필요
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("sub").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

	@Override
	public String getName() {
		return attribute.get("name").toString();
	}

	// @Override
	// public String getPhoneNumber() {
	// 	return "";
	// }
}
