package study.datajpa.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : study.datajpa.repository
 * fileName       : UsernameOnlyDto
 * author         : kmy
 * date           : 12/15/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 12/15/23        kmy       최초 생성
 */
@Getter
@RequiredArgsConstructor
public class UsernameOnlyDto {
//생성자의 파라미터명으로 매칭.
    private final String username;
}
