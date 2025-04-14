package ssammudan.cotree.global.config.security.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.jwt.AccessTokenService;
import ssammudan.cotree.global.config.security.jwt.RefreshTokenService;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;

/**
 * PackageName : ssammudan.cotree.global.config.security.user
 * FileName    : CustomUserDetailsService
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;
	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;

	@Override
	public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
		return new CustomUser(member, null);
	}

	public CustomUser loadUserById(String id) throws UsernameNotFoundException {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
		// new UsernameNotFoundException("id에 맞는 멤버가 존재하지 않습니다.");
		return new CustomUser(member, null);
	}

	public CustomUser loadUserByAccessToken(String accessToken) {
		try {
			Claims claimsFromToken = accessTokenService.getClaimsFromToken(accessToken);
			String id = claimsFromToken.get("mid", String.class);
			Member member = memberRepository.findById(id)
				.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
			return new CustomUser(member, null);
		} catch (Exception e) { // Jwt는 굳이 예외처리하지 않음. 이 후 필터로 전달하기 위해
			return null;
		}
	}

	public CustomUser loadUserByRefreshToken(String refreshToken) {
		try {
			Claims claimsFromToken = refreshTokenService.getClaimsFromToken(refreshToken);
			String id = claimsFromToken.get("mid", String.class);
			Member member = memberRepository.findById(id)
				.orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
			return new CustomUser(member, null);
		} catch (Exception e) { // Jwt는 굳이 예외처리하지 않음. 이 후 필터로 전달하기 위해
			return null;
		}
	}

}
