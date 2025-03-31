package ssammudan.cotree.model.member.member.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssammudan.cotree.global.entity.BaseEntity;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

@Entity
@Getter
@Table(name = "member")
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor // (access = AccessLevel.PUBLIC) // Test를 위해 public으로 변경
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "nickname", nullable = false, unique = true) // todo : DDL에서 unique 제약조건 추가
	private String nickname;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number", nullable = false, unique = true) // todo : DDL에서 unique 제약조건 추가
	private String phoneNumber;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	@Builder.Default
	private MemberRole role = MemberRole.USER;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_status", nullable = false)
	@Builder.Default
	private MemberStatus memberStatus = MemberStatus.ACTIVE;
}
