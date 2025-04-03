package ssammudan.cotree.global.config.security.oauth;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ssammudan.cotree.global.config.security.oauth.dto.KakaoResponse;
import ssammudan.cotree.global.config.security.oauth.dto.OAuth2Response;
import ssammudan.cotree.global.config.security.user.CustomUser;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.oauth.entity.OAuth;
import ssammudan.cotree.model.member.oauth.repository.OAuthRepository;

/**
 * PackageName : ssammudan.cotree.global.config.security.oauth
 * FileName    : OAuth2UserService
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
	private final MemberRepository memberRepository;
	private final OAuthRepository oAuthRepository;

	@Override
	// OAuth2UserRequest : OAuth2 프로토콜을 통해 인증된(AccessToken 발급) 사용자의 요청 정보(Scope, clientID, clientSecret 등)를 담고 있는 객체
	public CustomUser loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// OAuth2USer : OAuth2 인증이 완료된 후, 인증 서버에서 제공한 사용자 정보를 담고 있는 객체, 권한 정보도 포함됨
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// registrationId : 사용자의 요청을 받는 인증서버(ex, google, naver, ...)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// OAuth2Reponse : 인증 서버 별로 다른 제공된 사용자 정보를 형식화 하기 위한 커스텀 인터페이스
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "제공된 인증 정보가 유효하지 않습니다.");
		}

		// oauth 테이블 조회
		String email = oAuth2Response.getEmail();
		String providerId = oAuth2Response.getProviderId();

		Optional<OAuth> oAuth = oAuthRepository.findByProviderId(providerId);
		if (oAuth.isPresent()) {
			Member member = oAuth.get().getMember();
			return new CustomUser(member, oAuth2Response);
		}

		// 이메일로 회원 조회
		Optional<Member> member = memberRepository.findByEmail(email);

		// 일치하는 정보가 없으면 예외 발생
		if (member.isEmpty()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("user_not_found"), "일치하는 회원이 없습니다.");
		}
		// 일치하는 회원이 있으면 oauth 저장
		OAuth oAuthEntity = new OAuth(member.get(), oAuth2Response);
		oAuthRepository.save(oAuthEntity);
		return new CustomUser(member.get(), oAuth2Response);
	}
}

