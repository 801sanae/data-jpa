package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //JpaRepository<Entity, PK의 type>

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //메소드 이름을 분석해서 JPQL을 생성하고 실행
}
