package ssammudan.cotree.integration.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import ssammudan.cotree.model.common.developmentposition.entity.DevelopmentPosition;
import ssammudan.cotree.model.common.developmentposition.repository.DevelopmentPositionRepository;
import ssammudan.cotree.model.common.techstack.entity.TechStack;
import ssammudan.cotree.model.common.techstack.repository.TechStackRepository;
import ssammudan.cotree.model.member.member.entity.Member;
import ssammudan.cotree.model.member.member.repository.MemberRepository;
import ssammudan.cotree.model.member.member.type.MemberRole;
import ssammudan.cotree.model.member.member.type.MemberStatus;

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

	@Transactional
	public String setData() {
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

		Member member1 = memberRepository.save(member);
		String memberID = member1.getId();
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
		return memberID;
	}

}
