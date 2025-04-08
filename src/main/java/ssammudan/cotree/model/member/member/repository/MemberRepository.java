package ssammudan.cotree.model.member.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByUsernameAndPhoneNumber(String username, String phoneNumber);
}
