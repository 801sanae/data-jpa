package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom, JpaSpecificationExecutor<Member>{
//public interface MemberRepository extends Repository<Member, Long> {
    //JpaRepository<Entity, PK의 type>

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //메소드 이름을 분석해서 JPQL을 생성하고 실행

//    @Query(name = "Member.findByUsername")
//    List<Member> findByUsername(@Param("username") String name); //Collection return

    //실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m") // 단순히 값 하나를 조회
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)" + "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username = :name") // 위치 기반
//    @Query("select m from Member m where m.username = ?0") // 이름 기반
//    Member findMembers(@Param("name") String username); // 단건 return
    Optional<Member> findMembers(@Param("name") String username); // optional return

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

/*
    Page<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 count쿼리 결과를 포함하는 페이징, count 쿼리 사용
    Slice<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 안함
    List<Member> findByUsername(String name, Pageable pageable); // count 쿼리 사용 안함
    List<Member> findByUsername(String, Sort sort); // 정렬기능
*/
    @Query(value = "select m from Member m",
            countQuery = "select count(m) from Member m")// count query 분리 가능,,
    Page<Member> findByAge(int age, Pageable pageable); // count 쿼리 포함하는 페이징
//    Slice<Member> findByAge(int age, Pageable pageable); // TotalCount Query X
//List<Member> findByAge(int age, Pageable pageable); // List로도 반환가능

/*
 * 1. springboot 3.0 이상은 hibernate 6 적용 -> 의미없는 left join 최적화 -> 아래와 같은 경우 SQL left join 하지않음.
    @Query(value = "select m from Member m left join m.team t")
    Page<Member> findByAge(int age, Pageable pageable);

 * 2. Member 와 Team 을 조인을 하지만 사실 이 쿼리를 Team 을 전혀 사용하지 않는다.
 *    select 절이나, where 절에서 사용하지 않는다.
 *    결국 select m from Member m 과 다를께 없음.

 * 3. JPA가 최적화해버림. join없이 SQL생성함.

 * 4. 그럼에도 member, team을 원쿼리로 조회하고 싶다면, fetch join 사용..
 * select m from Member m left join fetch m.Team t


*/
    @Modifying(clearAutomatically = true)//<- 이 쿼리 이후 clear 실행...
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


//n+1 -> fetch join
// member, team,, proxy team객체, 사용할떄 다시 team 관련 쿼리가 실행된다. <--- N+1 문제
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    // fetch join은 left outer join이 기본,,이라함
    //entity graph <- jpa 표준스펙? @NameEntityGraph
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername();

    //hibernate에서 제공
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",  value = "true")) // <- 최적화,, Snapshot 안만듬
    Member findReadOnlyByUsername(String username);

    //select for update? LOCK
    //java persistence
    //JPA가 제공하는 Annotation을 통해 사용 가능.
    //Transation , Lock 책 참고,,
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

//interface기반의 projection
//    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

//class기반의 projection
    List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);
}
