package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

/**
 * packageName    : study.datajpa.entity
 * fileName       : Member
 * author         : kmy
 * date           : 10/27/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/27/23        kmy       최초 생성
 */

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private int age;

    public Member(String username){
        this.username = username;
    }
}
