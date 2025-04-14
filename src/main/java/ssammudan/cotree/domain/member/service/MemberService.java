package ssammudan.cotree.domain.member.service;

import ssammudan.cotree.domain.member.dto.MemberOrderResponse;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsRequest;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsResponse;
import ssammudan.cotree.domain.member.dto.MemberRecoverSmsVerifyRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoRequest;
import ssammudan.cotree.domain.member.dto.info.MemberInfoResponse;
import ssammudan.cotree.domain.member.dto.signin.MemberSigninRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsRequest;
import ssammudan.cotree.domain.member.dto.signup.MemberSignupSmsVerifyRequest;
import ssammudan.cotree.domain.member.type.OrderProductCategoryType;
import ssammudan.cotree.global.response.PageResponse;
import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberService {
	Member signUp(MemberSignupRequest member);

	Member signIn(MemberSigninRequest request);

	Member updateMember(String memberId, MemberInfoRequest request);

	Member findById(String memberId);

	MemberInfoResponse getMemberInfo(String id);

	void sendSignupCode(MemberSignupSmsRequest request);

	void verifySignupCode(MemberSignupSmsVerifyRequest request);

	void recoveryLoginId(MemberRecoverSmsRequest request);

	MemberRecoverSmsResponse verifyRecoverLoginId(MemberRecoverSmsVerifyRequest request);

	PageResponse<MemberOrderResponse> getOrderList(int page, int size, OrderProductCategoryType type, String id);

	void updatePassword(String email, String password);
}
