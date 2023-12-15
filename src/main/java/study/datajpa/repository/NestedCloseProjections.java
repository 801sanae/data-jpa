package study.datajpa.repository;

public interface NestedCloseProjections {

    String getUsername(); // 1. 최적화된체로 조회,, -> 정확히 username
    TeamInfo getTeam(); // 2. 모두 조회 entity로 불로옴,,

    // 3. left join

    interface TeamInfo{
        String getName();
    }
}
