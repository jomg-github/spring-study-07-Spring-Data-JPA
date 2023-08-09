package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.dto.MemberDTO;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.Team;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

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

    @Test
    void findByNameAndAgeGreaterThan() {
        // given
        Member memberA = memberRepository.save(new Member("조민기", 10));
        Member memberB = memberRepository.save(new Member("이승우", 20));
        Member memberC = memberRepository.save(new Member("이병건", 30));

        // when
        List<Member> findMembers = memberRepository.findByNameContainingAndAgeGreaterThan("이", 19);

        // then
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    void findByName_NamedQuery() {
        // given
        Member memberA = memberRepository.save(new Member("조민기", 10));

        // when
        List<Member> members = memberRepository.findByNameNamedQuery("조민기");

        // then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0)).isEqualTo(memberA);
    }

    @Test
    void 리포지토리_쿼리_테스트_1_쿼리직접정의() {
        // given
        Member memberA = memberRepository.save(new Member("조민기", 10));
        Member memberB = memberRepository.save(new Member("조민기", 20));
        Member memberC = memberRepository.save(new Member("조민기", 30));

        // when
        List<Member> findMembers = memberRepository.findMemberByQuery("조민기", 10);

        // then
        assertThat(findMembers.get(0)).isEqualTo(memberA);
    }

    @Test
    void 리포지토리_쿼리_테스트_2_쿼리직접정의() {
        // given
        memberRepository.save(new Member("조민기1", 10));
        memberRepository.save(new Member("조민기2", 20));
        memberRepository.save(new Member("조민기3", 20));

        // when
        List<String> allMemberNames = memberRepository.findAllMemberNames();

        // then
        System.out.println("allMemberNames = " + allMemberNames);
    }

    @Test
    void 리포지토리_쿼리_테스트_3_DTO직접조회() {
        // given
        Team team = new Team("TEAM A");
        teamRepository.save(team);

        memberRepository.save(new Member("조민기1", 10, team));
        memberRepository.save(new Member("조민기2", 20, team));
        memberRepository.save(new Member("조민기3", 20, team));

        // when
        List<MemberDTO> memberDTOs = memberRepository.findMemberDTOs();

        // then
        System.out.println("memberDTOs = " + memberDTOs);
    }

    @Test
    void 리포지토리_쿼리_테스트_4_collection_파라미터_바인딩() {
        // given
        Team team = new Team("TEAM A");
        teamRepository.save(team);

        memberRepository.save(new Member("조민기1", 10, team));
        memberRepository.save(new Member("조민기2", 20, team));
        memberRepository.save(new Member("조민기3", 20, team));

        // when
        List<String> allMemberNames = memberRepository.findAllMemberNames();
        List<Member> membersByNames = memberRepository.findByNames(allMemberNames);

        // then
        System.out.println("membersByNames = " + membersByNames);
    }

    @Test
    void 반환타입_테스트() {
        // given
        Team team = new Team("TEAM A");
        teamRepository.save(team);

        memberRepository.save(new Member("조민기1", 10, team));
        memberRepository.save(new Member("조민기1", 10, team));
        Member member = memberRepository.save(new Member("조민기2", 20, team));
        memberRepository.save(new Member("조민기3", 20, team));

        // when
        List<Member> 조민기1 = memberRepository.findMembersByName("조민기1");
        Member 조민기2 = memberRepository.findMemberByName("조민기2");
        Optional<Member> 조민기999 = memberRepository.findOptionalMemberByName("조민기999");

        // then
        assertThat(조민기1.size()).isEqualTo(2);
        assertThat(조민기2).isEqualTo(member);
        assertThatThrownBy(() -> 조민기999.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 페이징_테스트() {
        // given
        memberRepository.save(new Member("조민기1", 10));
        memberRepository.save(new Member("조민기2", 20));
        memberRepository.save(new Member("조민기3", 30));
        memberRepository.save(new Member("조민기4", 40));
        memberRepository.save(new Member("조민기5", 50));
        memberRepository.save(new Member("조민기6", 5));
        memberRepository.save(new Member("조민기7", 8));

        // when
        Integer age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        Page<Member> page = memberRepository.findPagedMembersByAgeGreaterThanEqual(age, pageRequest);
        Slice<Member> slice = memberRepository.findSlicedMembersByAgeGreaterThanEqual(age, pageRequest);
        List<Member> members = memberRepository.findMembersByAgeGreaterThanEqual(age, pageRequest);
        Page<MemberDTO> dtoPage = page.map(m -> new MemberDTO(m.getId(), m.getName()));

        List<Member> membersByAge = page.getContent();

        // then
        assertThat(membersByAge.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
}