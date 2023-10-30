package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

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
        assertThat(findMember).isEqualTo(member); // JPA entity 동일성 보장
    }

    @Test
    public void basicCRUD(){

        //save
        Member m1 = new Member("member1");
        Member m2 = new Member("member2");
        memberJpaRespository.save(m1);
        memberJpaRespository.save(m2);

        //단건 select
        Member findM1 = memberJpaRespository.findById(m1.getId()).get();
        Member findM2 = memberJpaRespository.findById(m2.getId()).get();

        assertThat(findM1).isEqualTo(m1);
        assertThat(findM2).isEqualTo(m2);

        //list select
        List<Member> all = memberJpaRespository.findAll();
        assertThat(all.size()).isEqualTo(2);

        Long count = memberJpaRespository.count();
        assertThat(count).isEqualTo(2);

        //delete
        memberJpaRespository.delete(m1);
        memberJpaRespository.delete(m2);

        Long deletedCount = memberJpaRespository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
}