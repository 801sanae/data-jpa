package study.datajpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;

/**
 * packageName    : study.datajpa.repository
 * fileName       : MemberSpec
 * author         : kmy
 * date           : 12/15/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 12/15/23        kmy       최초 생성
 */
public class MemberSpec {

    public static Specification<Member> teamName(final String teamName){
        return new Specification<Member>(){

            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if(StringUtils.isEmpty(teamName)){
                    return null;
                }

                Join<Object, Object> t = root.join("team", JoinType.INNER);
                return builder.equal(t.get("name"), teamName);
            }
        };
    }

    public static Specification<Member> userName(final String username){
        return (Specification<Member>) (root,query,builder) -> builder.equal(root.get("username"), username);
    }
}
