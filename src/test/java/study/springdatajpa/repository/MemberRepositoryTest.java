package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원저장() {
        // given
        // when
        Member saveMember = memberRepository.save(new Member("조민기"));
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(NoSuchElementException::new);

        // then
        assertThat(findMember).isEqualTo(saveMember);
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
    }


    @Test
    void CRUD() {
        // given
        Member memberA = memberRepository.save(new Member("MEMBER A"));
        Member memberB = memberRepository.save(new Member("MEMBER B"));

        // when
        // then
        Member findMemberA = memberRepository.findById(memberA.getId()).orElseThrow(NoSuchElementException::new);
        Member findMemberB = memberRepository.findById(memberB.getId()).orElseThrow(NoSuchElementException::new);
        List<Member> allMembers = memberRepository.findAll();

        assertThat(memberA).isEqualTo(findMemberA);
        assertThat(memberB).isEqualTo(findMemberB);
        assertThat(allMembers.size()).isEqualTo(2);

        memberRepository.delete(memberA);
        memberRepository.delete(memberB);
        Long total = memberRepository.count();
        assertThat(total).isEqualTo(0L);
    }
}