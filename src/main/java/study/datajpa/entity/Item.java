package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * packageName    : study.datajpa.entity
 * fileName       : Item
 * author         : kmy
 * date           : 12/15/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 12/15/23        kmy       최초 생성
 */
@Getter
@Entity
public class Item {
    @GeneratedValue
    @Id
    private Long id; // 객체는 null , 자바 기본 타입일 때 0 ex)long

}
