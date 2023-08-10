package study.springdatajpa.repository;

public interface MemberProjection {
    Long getMemberId();

    String getMemberName();

    String getTeamName();
}
