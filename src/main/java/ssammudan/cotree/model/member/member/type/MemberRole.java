package ssammudan.cotree.model.member.member.type;

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
}
