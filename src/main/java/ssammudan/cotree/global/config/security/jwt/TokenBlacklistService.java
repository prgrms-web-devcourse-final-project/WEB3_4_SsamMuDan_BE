package ssammudan.cotree.global.config.security.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PackageName : ssammudan.cotree.global.config.security.jwt
 * FileName    : TokenBlacklistService
 * Author      : hc
 * Date        : 25. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 1.     hc               Initial creation
 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String BLACKLIST_PREFIX = "blacklist:";

	// Refresh Token을 블랙리스트에 추가 (만료시간 설정)
	public void addToBlacklist(String refreshToken, long expirationSeconds) {
		redisTemplate.opsForValue()
			.set(BLACKLIST_PREFIX + refreshToken, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
	}

	// Refresh Token이 블랙리스트에 있는지 확인
	public boolean isBlacklisted(String refreshToken) {
		return redisTemplate.opsForValue().get(BLACKLIST_PREFIX + refreshToken) != null;
	}
}
