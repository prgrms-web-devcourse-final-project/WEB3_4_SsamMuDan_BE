package ssammudan.cotree.model.member.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * PackageName : ssammudan.cotree.model.member.type
 * FileName    : MemberRole
 * Author      : hc
 * Date        : 25. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 28.     hc               Initial creation
 */
public enum MemberRole {
	USER("user"),
	HUNTER("hunter"),
	ADMIN("admin");

	private final String role;

	// 생성자
	MemberRole(String role) {
		this.role = role;
	}

	// role 값을 반환하는 메서드
	public String getRole() {
		return role;
	}

	// 문자열을 enum으로 변환하는 팩토리 메서드
	@JsonCreator
	public static MemberRole from(String value) {
		for (MemberRole role : MemberRole.values()) {
			if (role.role.equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("올바르지 않은 역할입니다: " + value);
	}
}
