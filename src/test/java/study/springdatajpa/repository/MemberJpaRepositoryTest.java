package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void 회원저장() {
        // given
        // when
        Member saveMember = memberJpaRepository.save(new Member("조민기"));
        Member findMember = memberJpaRepository.findById(saveMember.getId()).orElseThrow(NoSuchElementException::new);

        // then
        assertThat(findMember).isEqualTo(saveMember);
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
    }

    @Test
    void CRUD() {
        // given
        Member memberA = memberJpaRepository.save(new Member("MEMBER A"));
        Member memberB = memberJpaRepository.save(new Member("MEMBER B"));

        // when
        // then
        Member findMemberA = memberJpaRepository.findById(memberA.getId()).orElseThrow(NoSuchElementException::new);
        Member findMemberB = memberJpaRepository.findById(memberB.getId()).orElseThrow(NoSuchElementException::new);
        List<Member> allMembers = memberJpaRepository.findAll();

        assertThat(memberA).isEqualTo(findMemberA);
        assertThat(memberB).isEqualTo(findMemberB);
        assertThat(allMembers.size()).isEqualTo(2);

        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);
        Long total = memberJpaRepository.count();
        assertThat(total).isEqualTo(0L);
    }

    @Test
    void findByNameAndAgeGreaterThan() {
        // given
        Member memberA = memberJpaRepository.save(new Member("조민기", 10));
        Member memberB = memberJpaRepository.save(new Member("이승우", 20));
        Member memberC = memberJpaRepository.save(new Member("이병건", 30));

        // when
        List<Member> findMembers = memberJpaRepository.findByNameContainingAndAgeGreaterThan("이", 19);

        // then
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    void findByName_NamedQuery() {
        // given
        Member memberA = memberJpaRepository.save(new Member("조민기", 10));

        // when
        List<Member> members = memberJpaRepository.findByNameNamedQuery("조민기");

        // then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0)).isEqualTo(memberA);
    }
}