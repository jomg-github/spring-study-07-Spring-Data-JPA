package study.springdatajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

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

    @Test
    void JpaBaseEntity_MappedSuperClass() throws InterruptedException {
        // given
        Member member = memberRepository.save(new Member("MEMBER A")); // @PrePersist
        Thread.sleep(100);
        member.setName("MEMBER A(modified)");
        em.flush(); // @PreUpdate
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(NoSuchElementException::new);

        // then
        System.out.println("findMember = " + findMember);
        System.out.println("    findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("    findMember.getUpdatedDate() = " + findMember.getUpdatedDate());
        System.out.println("    findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("    findMember.getUpdatedBy() = " + findMember.getUpdatedBy());
    }

}