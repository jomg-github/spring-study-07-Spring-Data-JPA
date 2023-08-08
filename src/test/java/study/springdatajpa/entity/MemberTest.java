package study.springdatajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void 회원_엔티티_연관관계_테스트() {
        // given
        // when
        Team teamA = new Team("TEAM A");
        Team teamB = new Team("TEAM B");
        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("MEMBER A", 31, teamA);
        Member memberB = new Member("MEMBER B", 30, teamA);
        Member memberC = new Member("MEMBER C", 20, teamB);
        Member memberD = new Member("MEMBER D", 18, teamB);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        em.flush();
        em.clear();

        // then
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println(member.getTeam() + " >>>>> member = " + member);
        }
    }

}