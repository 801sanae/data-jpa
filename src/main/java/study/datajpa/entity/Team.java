package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","name"}) // 연관관계 필드는 뺴주는게 좋다? 순환참조같이?
public class Team {

    @Id
    @GeneratedValue
    @Column(name="team_id")
    private Long id;
    private String name;
    //FK가 없는쪽에?
    @OneToMany(mappedBy = "team")
//    @JoinColumn(name = "member_id")
    private List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }

}
