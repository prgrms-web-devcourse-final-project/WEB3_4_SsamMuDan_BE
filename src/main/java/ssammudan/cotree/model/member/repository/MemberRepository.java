package ssammudan.cotree.model.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssammudan.cotree.model.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
