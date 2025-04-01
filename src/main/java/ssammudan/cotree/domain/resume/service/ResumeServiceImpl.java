package ssammudan.cotree.domain.resume.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ssammudan.cotree.domain.resume.dto.BasicInfoResponse;
import ssammudan.cotree.domain.resume.dto.CareerInfoResponse;
import ssammudan.cotree.domain.resume.dto.PortfolioInfoResponse;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.domain.resume.dto.ResumeCreateResponse;
import ssammudan.cotree.domain.resume.dto.ResumeDetailResponse;
import ssammudan.cotree.domain.resume.dto.query.BasicInfoQueryDto;
import ssammudan.cotree.domain.resume.dto.query.TechStackInfo;
import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.recruitment.career.career.entity.Career;
import ssammudan.cotree.model.recruitment.career.career.repository.CareerRepository;
import ssammudan.cotree.model.recruitment.career.techstack.entity.CareerTechStack;
import ssammudan.cotree.model.recruitment.portfolio.portfolio.entity.Portfolio;
import ssammudan.cotree.model.recruitment.portfolio.portfolio.repository.PortfolioRepository;
import ssammudan.cotree.model.recruitment.portfolio.techstack.entity.PortfolioTechStack;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;
import ssammudan.cotree.model.recruitment.resume.resume.repository.ResumeRepository;

/**
 * PackageName : ssammudan.cotree.domain.resume.service
 * FileName    : ResumeService
 * Author      : kwak
 * Date        : 2025. 3. 28.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 28.     kwak               Initial creation
 */
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
	private final ResumeRepository resumeRepository;
	private final TechStackRepository techStackRepository;
	private final DevelopmentPositionRepository developmentPositionRepository;
	private final CareerRepository careerRepository;
	private final PortfolioRepository portfolioRepository;
	private final MemberRepository memberRepository;
	private final ResumeViewCountBuffer resumeViewCountBuffer;

	//todo 추후에 insert 작업 batchUpdate() 로 교체해서 테스트 전후 차이 비교 예정
	@Transactional
	@Override
	public ResumeCreateResponse register(ResumeCreateRequest request, String memberId) {
		// 기본정보에서 기술스택과 개발직무 get, resume 저장
		List<TechStack> basicTechStacks = techStackRepository.findByIds(request.basicInfo().techStackIds());
		List<DevelopmentPosition> developmentPositions = developmentPositionRepository
			.findByIds(request.basicInfo().developPositionIds());

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

		Resume resume = Resume.create(request, member, basicTechStacks, developmentPositions);
		Resume savedResume = resumeRepository.save(resume);

		// 커리어에서 기술스택 get, career 저장
		request.careerInfos().forEach(careerInfo -> {
			List<TechStack> careerTechStacks = techStackRepository.findByIds(careerInfo.techStackIds());
			Career career = Career.create(careerInfo, savedResume, careerTechStacks);
			careerRepository.save(career);
		});

		// 포트폴리오에서 기술스택 get, portfolio 저장
		request.portfolioInfos().forEach(portfolioInfo -> {
			List<TechStack> portfolioStacks = techStackRepository.findByIds(portfolioInfo.techStackIds());
			Portfolio portfolio = Portfolio.create(portfolioInfo, savedResume, portfolioStacks);
			portfolioRepository.save(portfolio);
		});

		return ResumeCreateResponse.from(savedResume);
	}

	@Transactional(readOnly = true)
	@Override
	public ResumeDetailResponse detail(Long id) {
		Resume resume = resumeRepository.findById(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_RESUME));

		// 기본정보에 있는 데이터 뽑기
		BasicInfoQueryDto basicInfoQueryDto = resumeRepository.findBasicInfoQueryDto(id)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_RESUME));

		List<String> positionNames = resumeRepository.findDevelopmentPositionNames(id);
		List<TechStackInfo> techStackInfos = resumeRepository.findTechStackInfos(id);
		BasicInfoResponse basicInfoResponse = BasicInfoResponse.of(basicInfoQueryDto, positionNames, techStackInfos);

		// 커리어에 있는 데이터 뽑기
		// 커리어 n 개 , 커리어 1 개당 n개의 techStacks
		// 커리어 1개 -> 커리어 careerTechStacks N개
		List<CareerInfoResponse> careerInfoResponses = careerRepository.findByResume(resume).stream()
			.map(career ->
				CareerInfoResponse.of(career, getTechStackInfos(career)))
			.toList();

		// 포트폴리오에 있는 데이터 뽑기
		List<PortfolioInfoResponse> portfolioInfoResponses = portfolioRepository.findByResume(resume).stream()
			.map(portfolio ->
				PortfolioInfoResponse.of(portfolio, getTechStackInfos(portfolio)))
			.toList();

		// 조회수 저장
		resumeViewCountBuffer.increaseViewCount(id);

		return ResumeDetailResponse.create(basicInfoResponse, careerInfoResponses, portfolioInfoResponses);
	}

	@Transactional
	public void bulkViewCount(Map<Long, Integer> viewCountData) {
		resumeRepository.bulkUpdateViewCount(viewCountData);
	}

	private List<TechStack> getTechStackInfos(Career career) {
		return career.getCareerTechStacks().stream()
			.map(CareerTechStack::getTechStack).toList();
	}

	private List<TechStack> getTechStackInfos(Portfolio portfolio) {
		return portfolio.getPortfolioTechStacks().stream()
			.map(PortfolioTechStack::getTechStack).toList();
	}
}
