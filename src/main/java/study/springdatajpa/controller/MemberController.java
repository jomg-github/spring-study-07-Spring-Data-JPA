package study.springdatajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springdatajpa.dto.MemberDTO;
import study.springdatajpa.entity.Member;
import study.springdatajpa.repository.MemberRepository;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members/{id}")
    public String findMemberV1(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return member.getName();
    }

    @GetMapping("/v2/members/{id}")
    public String findMemberV2(@PathVariable("id") Member member) {
        return member.getName();
    }

    @GetMapping("/members")
    public Page<MemberDTO> list(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(member -> new MemberDTO(member.getId(), member.getName()));
    }

//    @PostConstruct
//    public void init() {
//        for (int i = 1; i <= 1000; i++) {
//            memberRepository.save(new Member("MEMBER " + i));
//        }
//    }
}
