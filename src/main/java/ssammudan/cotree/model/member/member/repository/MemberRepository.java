package ssammudan.cotree.model.member.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssammudan.cotree.model.member.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
