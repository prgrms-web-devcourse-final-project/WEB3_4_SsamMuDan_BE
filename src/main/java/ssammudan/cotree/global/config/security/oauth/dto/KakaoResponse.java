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

	private final Map<String, Object> attributes;
	private final Map<String, Object> kakaoAccount;
	private final Map<String, Object> profile;

	public KakaoResponse(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		this.profile = (Map<String, Object>)kakaoAccount.get("profile");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		return kakaoAccount.get("email").toString();
	}

	@Override
	public String getName() {
		return profile.get("nickname").toString();
	}

	public String getProfileUrl() {
		return profile.get("profile_image_url").toString();
	}

}
