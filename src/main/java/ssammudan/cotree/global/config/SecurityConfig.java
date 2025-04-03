package ssammudan.cotree.global.config;

import static org.springframework.http.HttpMethod.*;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.global.config.security.exception.CustomAccessDeniedHandler;
import ssammudan.cotree.global.config.security.exception.CustomAuthenticationEntryPoint;
import ssammudan.cotree.global.config.security.filter.JwtAuthenticationFilter;
import ssammudan.cotree.infra.frontend.FrontendConfig;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final FrontendConfig frontendConfig;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// ✅ 보안 관련 설정 (CSRF, CORS, 세션)
				.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (JWT 사용 시 불필요)
				.cors(cors -> corsConfigurationSource()) // CORS 설정 적용
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 (JWT 방식)

				// ✅ 필터 설정 (JWT 인증 필터 → UsernamePasswordAuthenticationFilter)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				// ✅ 인증 및 접근 권한 설정
				.authorizeHttpRequests(authorize -> authorize
						// H2 콘솔 허용
						.requestMatchers("/h2-console/**").permitAll()
						// 프론트엔드에서 적용될 예외 포인트 설정
						.requestMatchers("/error", "/favicon.ico").permitAll()
						// Swagger 문서 접근 허용
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
								"/swagger-ui.html").permitAll()
						.requestMatchers(GET, "/actuator/health").permitAll()

						// MEMBER Domain
						.requestMatchers(POST, "/api/v1/member/signup").permitAll()
						.requestMatchers(POST, "/api/v1/member/signin").permitAll()

						// Category Domain
						.requestMatchers(GET, "/api/v1/category/**").permitAll()

						// Comment Domain
						// 해당 없음. 모두 인증 필요

						// Community Domain
						.requestMatchers(GET, "/api/v1/community/board").permitAll()
						.requestMatchers(GET, "/api/v1/community/board/**").permitAll()

						// Education / TechBook Domain
						.requestMatchers(GET, "/api/v1/education/techbook/**").permitAll()
						.requestMatchers(GET, "/api/v1/education/techbook").permitAll()

						// Education / TechTube Domain
						// 해당 없음. 모두 인증 필요

					// Education / Review Domain
					.requestMatchers(GET, "/api/v1/education/review/**").permitAll()
					.requestMatchers(GET, "/api/v1/education/review").permitAll()

						// Resume Domain
						// .requestMatchers(GET, "/api/v1/recruitment/resume/**").permitAll()

						// Project Domain
					.requestMatchers(GET, "/api/v1/project/team/hot").permitAll()
					.requestMatchers(GET, "/api/v1/project/team/{projectId}").permitAll()

						// Upload Domain
						// 해당 없음. 모두 인증 필요

					// Like Domain
					// 해당 없음. 모두 인증 필요

						.anyRequest().authenticated()) // 그 외 요청은 인증 필요

				// ✅ 기본 인증 방식 비활성화 (JWT 사용)
				.httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
				.formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화

				// ✅ 예외 처리 설정
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(authenticationEntryPoint) // 인증이 수행되지 않았을 때 호출되는 핸들러
						.accessDeniedHandler(accessDeniedHandler)); // 접근 권한이 없을 때 호출되는 핸들러

		return http.build();
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// 허용할 오리진 설정
		configuration.setAllowedOrigins(
				List.of(frontendConfig.getFrontendUrl())); // 프론트 엔드
		// 허용할 HTTP 메서드 설정
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 프론트 엔드 허용 메서드
		// 자격 증명 허용 설정
		configuration.setAllowCredentials(true);
		// 허용할 헤더 설정
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		configuration.setExposedHeaders(
				List.of("Set-Cookie"));

		// CORS 설정을 소스에 등록
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
