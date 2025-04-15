package ssammudan.cotree.integration.factory;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import ssammudan.cotree.domain.resume.dto.BasicInfo;
import ssammudan.cotree.domain.resume.dto.CareerInfo;
import ssammudan.cotree.domain.resume.dto.PortfolioInfo;
import ssammudan.cotree.domain.resume.dto.ResumeCreateRequest;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;
import ssammudan.cotree.model.recruitment.resume.resume.entity.Resume;
import ssammudan.cotree.model.recruitment.resume.resume.repository.ResumeRepository;

/**
 * PackageName : ssammudan.cotree.domain.resume.service
 * FileName    : ResumeTestHelper
 * Author      : kwak
 * Date        : 2025. 3. 31.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 3. 31.     kwak               Initial creation
 */
@Component
public class ResumeDataFactory {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TechStackRepository techStackRepository;

	@Autowired
	private DevelopmentPositionRepository developmentPositionRepository;

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ResumeRepository resumeRepository;

	@Transactional
	public Member setData() {
		// 테스트 전 기존 데이터 삭제
		memberRepository.deleteAll();
		techStackRepository.deleteAll();
		developmentPositionRepository.deleteAll();

		// ID 시퀀스 초기화 1부터 시작
		entityManager.createNativeQuery("ALTER TABLE tech_stack ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE development_position ALTER COLUMN id RESTART WITH 1")
			.executeUpdate();

		Member member = new Member(
			null,
			"test@example.com",
			"김테스트",
			"테스터",
			"password1234",
			"010-1234-5678",
			"https://example.com/profile/default.jpg",
			MemberRole.USER,
			MemberStatus.ACTIVE);

		Member saverMember = memberRepository.save(member);
		String memberID = saverMember.getId();
		memberRepository.flush();

		Map<String, String> techStackData = new HashMap<>();
		techStackData.put("Java", "https://worldvectorlogo.com/download/java.svg");
		techStackData.put("Kotlin", "https://worldvectorlogo.com/download/kotlin-1.svg");
		techStackData.put("Spring", "https://worldvectorlogo.com/download/spring-3.svg");
		techStackData.put("Spring Boot", "https://worldvectorlogo.com/download/spring-boot-1.svg");
		techStackData.put("JavaScript", "https://worldvectorlogo.com/download/javascript-2.svg");
		techStackData.put("React", "https://worldvectorlogo.com/download/react-2.svg");
		techStackData.put("Typescript", "https://worldvectorlogo.com/download/typescript.svg");
		techStackData.put("Django", "https://worldvectorlogo.com/download/django.svg");
		techStackData.put("MySQL", "https://worldvectorlogo.com/download/mysql-3.svg");
		techStackData.put("MariaDB", "https://worldvectorlogo.com/download/mariadb.svg");
		techStackData.put("PostgreSQL", "https://worldvectorlogo.com/download/postgresql.svg");
		techStackData.put("MongoDB", "https://worldvectorlogo.com/download/mongodb-icon-1.svg");
		techStackData.put("AWS", "https://worldvectorlogo.com/download/aws-2.svg");
		techStackData.put("Node.js", "https://worldvectorlogo.com/download/nodejs.svg");

		techStackData.entrySet().stream()
			.map(e ->
				TechStack.create(e.getKey(), e.getValue()))
			.forEach(techStackRepository::save);

		techStackRepository.flush();

		List<String> positionNames = Arrays.asList("프론트엔드", "백엔드", "풀스택");
		positionNames.stream()
			.map(DevelopmentPosition::create)
			.forEach(developmentPositionRepository::save);

		developmentPositionRepository.flush();

		// entityManager.flush();
		// entityManager.clear();
		return saverMember;
	}

	public List<Resume> createAndSaveResume(List<Member> memberList, List<TechStack> saveTechStackList, List<DevelopmentPosition> saveDevPos) {
		LocalDate startDate = LocalDate.of(2022, Month.JANUARY, 1);
		LocalDate endDate = LocalDate.of(2023, Month.JANUARY, 1);

		CareerInfo careerInfo = CareerInfo
			.builder()
			.startDate(startDate)
			.endDate(endDate)
			.position("position1")
			.companyName("company1")
			.careerDescription("주요 성과")
			.isWorking(false)
			.techStackIds(saveTechStackList.stream().map(TechStack::getId).collect(Collectors.toSet()))
			.build();

		PortfolioInfo portfolioInfo = PortfolioInfo
			.builder()
			.startDate(startDate)
			.endDate(endDate)
			.projectName("project1")
			.projectDescription("project1 성과")
			.techStackIds(saveTechStackList.stream().map(TechStack::getId).collect(Collectors.toSet()))
			.build();

		BasicInfo basicInfo = BasicInfo
			.builder()
			.email("test1@email.com")
			.years(1)
			.introduction("자기소개")
			.developPositionIds(saveDevPos.stream().map(DevelopmentPosition::getId).collect(Collectors.toSet()))
			.techStackIds(saveTechStackList.stream().map(TechStack::getId).collect(Collectors.toSet()))
			.build();

		ResumeCreateRequest resumeCreateRequest = ResumeCreateRequest
			.builder()
			.basicInfo(basicInfo)
			.careerInfos(List.of(careerInfo))
			.portfolioInfos(List.of(portfolioInfo))
			.build();

		List<Resume> resumeList = new ArrayList<>();
		for (Member member : memberList) {
			Resume resume = Resume.create(resumeCreateRequest, member, saveTechStackList, saveDevPos, null);
			resumeList.add(resume);
		}

		return resumeRepository.saveAll(resumeList);
	}
}
