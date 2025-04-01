package ssammudan.cotree.global.config.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import ssammudan.cotree.global.config.security.user.CustomUser;

/**
 * PackageName : ssammudan.cotree.domain.jwt
 * FileName    : AccessTokenService
 * Author      : hc
 * Date        : 25. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 31.     hc               Initial creation
 */
@Service
public class AccessTokenService {

	@Value("${custom.jwt.accessToken.secret}")
	private String secret;

	SecretKey secretKey;

	@PostConstruct
	public void init() {
		// SecretKey는 @Value로 주입된 후 초기화됩니다.
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		this.expirationSeconds *= 1000; // ms -> s(초)
	}

	@Value("${custom.jwt.accessToken.expirationSeconds}")
	private long expirationSeconds;

	/**
	 * JWT 토큰 생성
	 * {
	 *   "sub": "홍길동",
	 *   "id": "a4a8571a-318a-4739-b775-7bf3a75e1458",
	 *   "role": [
	 *     {
	 *       "authority": "user"
	 *     }
	 *   ],
	 *   "iat": 1743471924,
	 *   "exp": 1743473124
	 * }
	 */
	public String generateToken(CustomUser customUser) throws JwtException {
		return Jwts.builder()
			.claim("mid", customUser.getId()) // memberId, id라는 이름은 JWT 표준 claim이므로 mid로 변경
			.subject(customUser.getName())
			.claim("role", customUser.getAuthorities())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + expirationSeconds))
			.signWith(secretKey)
			.compact();
	}

	public Claims getClaimsFromToken(String token) throws JwtException {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public void generateTokenToCookie(CustomUser customUser, HttpServletResponse response) throws JwtException {
		String accessToken = generateToken(customUser);

		ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
			.path("/")
			.httpOnly(true) // XSS 방지
			.secure(true) // SameSite=None;은 Secure=true;가 필수(정책)
			.sameSite("None") // 모든 도메인의 요청에서 쿠키 전송 (Lax는 GET 요청에 대해서만 쿠키 전송, Strict는 동일 출처에서만)
			.maxAge(expirationSeconds)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
