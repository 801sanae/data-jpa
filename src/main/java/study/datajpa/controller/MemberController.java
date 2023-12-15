package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

/**
 * packageName    : study.datajpa.controller
 * fileName       : MemberController
 * author         : kmy
 * date           : 12/15/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 12/15/23        kmy       최초 생성
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //Domain class Converter <- 비권장? PK
    //"조회용"으로만,,, transaction 범위가 없다?
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    // http://localhost:48080/members?page=0 0~20
    // http://localhost:48080/members?page=0&size=3 0~3
    // http://localhost:48080/members?page=0&size=3&sort=id sort 기준 id
    // http://localhost:48080/members?page=0&size=3&sort=username,desc sort 기준 username, desc로,,

    // page = 20 default

    // local 설정.. @PageableDefault
    /*
     * Page정보가 2개 이상일 경우
     *   @Qualifier("member") Pageable memberPageable,
     *   @Qualifier("order") Pageable orderPageable,,,
     *   로 해당Pagable에 대한 정보를 나눌수 있다.
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 4, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable){
        // 내부설계유출개념,, entity는 노출하지말자 entity -> dto
// #1
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> memberDtos = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
//        return memberDtos;

// #2
//        return memberRepository.findAll(pageable).map(member -> new MemberDto(member.getId(), member.getUsername(), null));

// #3
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }




//    @PostConstruct
    public void init(){
//        memberRepository.save(new Member("userA"));
        for (int i= 0 ; i< 100 ; i++){
            memberRepository.save(new Member("user"+i, i ));
        }

    }
}
