package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    // Open projection,,
    // Entity에서 모두 가져온후 처리
//    @Value("#{target.username + ' ' + target.age}")

    // close projection,,
    // 명확하게 필요한 데이터만 가져옴,
    String getUsername();
}
