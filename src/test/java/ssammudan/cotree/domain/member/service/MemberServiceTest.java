package ssammudan.cotree.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import ssammudan.cotree.domain.member.dto.MemberOrderResponse;
import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.type.OrderProductCategoryType;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;
import ssammudan.cotree.model.payment.order.category.repository.OrderCategoryRepository;

/**
 * PackageName : ssammudan.cotree.domain.member.service
 * FileName    : MemberServiceTest
 * Author      : hc
 * Date        : 25. 4. 2.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 4. 2.     hc               Initial creation
 */
@ExtendWith({MockitoExtension.class})
class MemberServiceTest {
	@InjectMocks
	private MemberServiceImpl memberService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private OrderCategoryRepository orderCategoryRepository;

	private Member mockMember;

	@BeforeEach
	void setUp() {
		mockMember = new Member(
			"id", "email", "username", "nickname", "password", "phoneNumber",
			"profile", MemberRole.USER, MemberStatus.ACTIVE);
	}

	@Test
	@DisplayName("회원 정보 찾기")
	void findById() {
		// given
		when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(mockMember));

		// when
		Member findMember = memberService.findById(mockMember.getId());

		// then
		assertThat(findMember.getId()).isEqualTo(mockMember.getId());
		assertThat(findMember.getEmail()).isEqualTo(mockMember.getEmail());
		assertThat(findMember.getUsername()).isEqualTo(mockMember.getUsername());
		assertThat(findMember.getNickname()).isEqualTo(mockMember.getNickname());
		assertThat(findMember.getPhoneNumber()).isEqualTo(mockMember.getPhoneNumber());
		assertThat(findMember.getProfileImageUrl()).isEqualTo(mockMember.getProfileImageUrl());
		assertThat(findMember.getRole()).isEqualTo(mockMember.getRole());
		assertThat(findMember.getMemberStatus()).isEqualTo(mockMember.getMemberStatus());
	}

	@Test
	@DisplayName("회원이 존재하지 않으면 예외 발생")
	void findByIdFail() {
		// given
		when(memberRepository.findById(any()))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.findById("not_exist_id"))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("회원을 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("회원 정보 수정")
	void updateMember() {
		// given
		when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(mockMember));
		// when(memberRepository.save(any())).thenReturn(mockMember);
		MemberInfoRequest memberInfoRequest = new MemberInfoRequest("username1", "nickname1", "profile1");

		// when
		Member updateMember = memberService.updateMember(mockMember.getId(), memberInfoRequest);

		// then
		verify(memberRepository, times(1)).findById(mockMember.getId());
		assertThat(updateMember.getUsername()).isEqualTo(memberInfoRequest.username());
		assertThat(updateMember.getNickname()).isEqualTo(memberInfoRequest.nickname());
		assertThat(updateMember.getProfileImageUrl()).isEqualTo(memberInfoRequest.profileImageUrl());
	}

	@Test
	@DisplayName("회원 정보 수정 시 회원이 존재하지 않으면 예외 발생")
	void updateMemberFail() {
		// given
		when(memberRepository.findById(any()))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.updateMember("not_exist_id",
			new MemberInfoRequest("username1", "nickname1", "profile1")))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("회원을 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("로그인")
	void signIn() {
		// given
		when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockMember));
		when(passwordEncoder.matches("rawPassword", "password")).thenReturn(true);
		MemberSigninRequest memberSigninRequest = new MemberSigninRequest(mockMember.getEmail(),
			"rawPassword");

		// when
		Member findMember = memberService.signIn(memberSigninRequest);

		// then
		assertThat(findMember.getId()).isEqualTo(mockMember.getId());
		assertThat(findMember.getEmail()).isEqualTo(mockMember.getEmail());
		assertThat(findMember.getUsername()).isEqualTo(mockMember.getUsername());
		assertThat(findMember.getNickname()).isEqualTo(mockMember.getNickname());
		assertThat(findMember.getPhoneNumber()).isEqualTo(mockMember.getPhoneNumber());
		assertThat(findMember.getProfileImageUrl()).isEqualTo(mockMember.getProfileImageUrl());
		assertThat(findMember.getRole()).isEqualTo(mockMember.getRole());
		assertThat(findMember.getMemberStatus()).isEqualTo(mockMember.getMemberStatus());
	}

	@Test
	@DisplayName("로그인 시 회원이 존재하지 않으면 예외 발생")
	void signInFail() {
		// given
		when(memberRepository.findByEmail(any()))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> memberService.signIn(new MemberSigninRequest("not_exist_email", "rawPassword")))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("인증정보가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("로그인 시 비밀번호가 일치하지 않으면 예외 발생")
	void signInFail2() {
		// given
		when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockMember));
		when(passwordEncoder.matches("rawPassword", "password")).thenReturn(false);
		MemberSigninRequest memberSigninRequest = new MemberSigninRequest(mockMember.getEmail(),
			"rawPassword");

		// when & then
		assertThatThrownBy(() -> memberService.signIn(memberSigninRequest))
			.isInstanceOf(GlobalException.class)
			.hasMessageContaining("인증정보가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("회원 정보 조회")
	void getMemberInfo() {
		// given
		when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(mockMember));

		// when
		MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(mockMember.getId());

		// then
		assertThat(memberInfoResponse).isNotNull();
		assertThat(memberInfoResponse.email()).isEqualTo(mockMember.getEmail());
		assertThat(memberInfoResponse.username()).isEqualTo(mockMember.getUsername());
		assertThat(memberInfoResponse.nickname()).isEqualTo(mockMember.getNickname());
		assertThat(memberInfoResponse.role()).isEqualTo(mockMember.getRole());
		assertThat(memberInfoResponse.createdAt()).isEqualTo(mockMember.getCreatedAt());
	}

	@Test
	@DisplayName("구매목록 조회 성공")
	void getOrderListSuccess() {
		// given
		int page = 0;
		int size = 12;
		OrderProductCategoryType type = OrderProductCategoryType.TECH_TUBE;
		String userId = "user123";

		Pageable pageable = PageRequest.of(page, size);

		when(orderCategoryRepository.existsById(type.getId())).thenReturn(true);

		List<MemberOrderResponse> content = List.of(
			new MemberOrderResponse(1L, "TECH_TUBE", "thumbnail1.jpg", "Java 강의", "홍길동"),
			new MemberOrderResponse(2L, "TECH_TUBE", "thumbnail2.jpg", "Spring 강의", "김철수")
		);

		Page<MemberOrderResponse> response = new PageImpl<>(content, pageable, content.size());

		when(memberRepository.getOrderList(pageable, type, userId)).thenReturn(response);

		// when
		PageResponse<MemberOrderResponse> result = memberService.getOrderList(page, size, type, userId);

		// then
		assertThat(2).isEqualTo(result.getTotalElements());
		assertThat(2).isEqualTo(result.getContent().size());
		assertThat("Java 강의").isEqualTo(result.getContent().get(0).title());
		assertThat("Spring 강의").isEqualTo(result.getContent().get(1).title());

		verify(orderCategoryRepository).existsById(type.getId());
		verify(memberRepository).getOrderList(pageable, type, userId);
	}

	@Test
	@DisplayName("주문 카테고리가 존재하지 않을 때 ORDER_CATEGORY_NOT_FOUND 발생")
	void getOrderListWithNotExistsOrderCategory() {
		// given
		int page = 0;
		int size = 12;
		OrderProductCategoryType type = OrderProductCategoryType.TECH_TUBE;
		String userId = "user123";

		when(orderCategoryRepository.existsById(type.getId())).thenReturn(false);

		// when
		// then
		assertThatThrownBy(() ->
			memberService.getOrderList(page, size, type, userId))
			.isInstanceOf(GlobalException.class)
				.hasMessageContaining("제품 카테고리를 찾을 수 없습니다.");

		verify(orderCategoryRepository).existsById(type.getId());
		verify(memberRepository, never()).getOrderList(any(), any(), any());
	}
}
