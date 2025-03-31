package ssammudan.cotree.domain.community.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import ssammudan.cotree.integration.SpringBootTestSupporter;

/**
 * PackageName : ssammudan.cotree.domain.community.controller
 * FileName    : CommunityControllerTest
 * Author      : Baekgwa
 * Date        : 2025-03-28
 * Description : Community Domain Controller Layer Test
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-03-28     Baekgwa               Initial creation
 */
@Transactional
class CommunityControllerTest extends SpringBootTestSupporter {

	@DisplayName("새로운 커뮤니티 글 작성")
	@Test
	//todo : 추후, Token 관련 Security 적용 시, Mock 유저로 변경하여 사용 필요.
	//todo : mock 유저 적용되어야 재대로된 테스트 가능하여 newBoard controller Test todo 적용.
	void createNewBoard() {
		// given

		// when

		// then
	}
}
