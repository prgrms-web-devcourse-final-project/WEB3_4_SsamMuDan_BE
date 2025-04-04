package ssammudan.cotree.model.member.oauth.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.config.security.oauth.dto.OAuth2Response;
import ssammudan.cotree.model.member.member.entity.Member;

/**
 * PackageName : ssammudan.cotree.model.member.oauth.entity
 * FileName    : OAuth
 * Author      : Baekgwa
 * Date        : 2025-03-29
 * Description : oauth table `JPA Entity`
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-29     Baekgwa               Initial creation
 */
@Entity
@Getter
@Table(name = "oauth")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth {

	public OAuth(Member member, OAuth2Response oAuth2Response) {
		this.member = member;
		this.providerName = oAuth2Response.getProvider();
		this.providerId = oAuth2Response.getProviderId();
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "provider_name", nullable = false)
	private String providerName;

	@Column(name = "provider_id", nullable = false)
	private String providerId;

	@CreatedDate
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;
}
