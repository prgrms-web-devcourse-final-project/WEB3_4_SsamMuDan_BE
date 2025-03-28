package ssammudan.cotree.model.member.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.type.MemberRole;
import ssammudan.cotree.model.member.type.MemberStatus;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor // (access = AccessLevel.PUBLIC) // Test를 위해 public으로 변경
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

	public static Member createSignUpMember(MemberSignupRequest request) {
		return Member.builder()
			.email(request.email())
			.password(request.password())
			.name(request.name())
			.nickname(request.nickname())
			.phoneNumber(request.phoneNumber()
			).build();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, updatable = false)
	private String id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true) // todo : DDL에서 unique 제약조건 추가
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true) // todo : DDL에서 unique 제약조건 추가
	private String phoneNumber;

	private String profileImageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private MemberRole role = MemberRole.USER;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private MemberStatus memberStatus = MemberStatus.ACTIVE;
}
