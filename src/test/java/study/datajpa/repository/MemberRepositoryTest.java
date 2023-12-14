package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("testMemberB");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();//optional

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findM1 = memberRepository.findById(member1.getId()).get();
        Member findM2 = memberRepository.findById(member2.getId()).get();
        assertThat(findM1).isEqualTo(member1);
        assertThat(findM2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void paging() throws Exception{
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(10, pageRequest); (limit+1) offset
//        List<Member> page = memberRepository.findByAge(10, pageRequest);

        // *** Entity를 반환 X -> EntityDTO를 통해 반환하자. ***
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

//        List<MemberDto> contentDto = toMap.getContent();
//        contentDto.size();
//        toMap.getTotalElements();
//        toMap.getTotalPages();


        //then
        List<Member> content = page.getContent(); // 조회된 데이터
        assertThat(content.size()).isEqualTo(3); // 조건의로 가져온값의 size

        assertThat(page.getTotalElements()).isEqualTo(5); // 조회된 데이터의 Total Count
        assertThat(page.getNumber()).isEqualTo(0); //페이지번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번쨰페이지인가?
        assertThat(page.hasNext()).isTrue(); // 다음페이지가 있나?


    }

    @Test
    public void builUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 41));

        //then
        int resultCount = memberRepository.bulkAgePlus(20);
        //벌크 연산 후 영속성 flush, clear 필요..
        em.flush();
        em.clear();

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
      //given
//      member1 -> teamA
//      member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();

        //when
//        List<Member> members = memberRepository.findAll(); // 1. N+1 문제 발생,,
//        List<Member> members = memberRepository.findMemberFetchJoin(); // 2. fetch join을 하게 되면 proxy객체가 아닌 실제 Team claa로 채운다.
        List<Member> members = memberRepository.findByUsername(); // 3. entity graph 사용시 내부적 fetch Join 사용?!
//N+1,, -> fetch join
        for (Member m : members){
            System.out.println("member = " + m.getUsername());
            System.out.println("member teamclass= " + m.getTeam().getClass()); // 일단 proxy객체로 대체한다.
            System.out.println("member team= " + m.getTeam().getName()); // 이떄 team에 대한 쿼리가 실행된다.
        }

    }

    @Test
    public void queryhint(){
        /*
            //given
            Member member1 = memberRepository.save(new Member("member1", 10));
            em.flush();
            em.clear(); //<- 영속성 캐시 삭제

            //when
            Member findMember = memberRepository.findById(member1.getId()).get(); // 원본과, 변경 두가지를 가지고 있다.
            findMember.setUsername("member2"); // 변경감지 update..

            em.flush();; // 상태체크, dirty cheking 변경감지,,,
         */
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member readOnlyByUserName = memberRepository.findReadOnlyByUserName("member1");
        readOnlyByUserName.setUsername("member2");

        em.flush(); //<-readOnly true라서 최적화,, 변경감지가 안됨.

    }
}