package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // <- 1. Transactional Test 끝난후 Rollback
@Transactional // 같은 Transaction상 영속성의 동일성 보장, 1차 캐시기능?, 트랜잭션이 다르면 다른 객체가 찾아진다.
@Rollback(value = false) // 2. @Rollback(value = false) 3. 실무에서는 뺴주는 방향으로~
class MemberJpaRespositoryTest {

    @Autowired
    MemberJpaRespository memberJpaRespository;

    @Test
    public void testMember(){
        Member member = new Member("testMember");
        Member saveMember = memberJpaRespository.save(member);

        Member findMember = memberJpaRespository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

}