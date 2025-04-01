package ssammudan.cotree.global.config.security.jwt;

import java.util.Date;
import java.util.UUID;

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
import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.user.CustomUser;

/**
 * PackageName : ssammudan.cotree.domain.jwt
 * FileName    : RefreshTokenService
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
public class RefreshTokenService {
	private final TokenBlacklistService tokenBlacklistService;

	@Value("${custom.jwt.refreshToken.secret}")
	private String secret;

	SecretKey secretKey;

	@PostConstruct
	public void init() {
		// SecretKey는 @Value로 주입된 후 초기화됩니다.
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		this.expirationSeconds *= 1000; // ms -> s(초)
	}

	@Value("${custom.jwt.refreshToken.expirationSeconds}")
	private long expirationSeconds;

	public String generateToken(CustomUser customUser) throws JwtException {
		return Jwts.builder()
			.claim("mid", customUser.getId()) // memberId, id라는 이름은 JWT 표준 claim이므로 mid로 변경
			.id(UUID.randomUUID().toString()) // blacklisting을 위한 고유한 id
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
		String refreshToken = generateToken(customUser);

		ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
			.path("/")
			.httpOnly(true) // XSS 방지
			.secure(true) // SameSite=None;은 Secure=true;가 필수(정책)
			.sameSite("None") // 모든 도메인의 요청에서 쿠키 전송 (Lax는 GET 요청에 대해서만 쿠키 전송, Strict는 동일 출처에서만)
			.maxAge(expirationSeconds)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public boolean isValidToken(String token) {
		boolean isBlacklist = tokenBlacklistService.isBlacklisted(token);
		if (isBlacklist) {
			return false;
		}

		try {
			Claims claims = getClaimsFromToken(token);
			return !claims.getExpiration().before(new Date());
		} catch (JwtException e) {
			return false;
		}
	}
}

