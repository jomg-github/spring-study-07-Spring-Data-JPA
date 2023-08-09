package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.dto.MemberDTO;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameContainingAndAgeGreaterThan(String name, Integer age);

    @Query(name = "Member.findByNameNamedQuery")
    List<Member> findByNameNamedQuery(@Param("name") String name);

    @Query("select m from Member m where m.name = :username and m.age = :age")
    List<Member> findMemberByQuery(@Param("username") String name, @Param("age") Integer age);

    @Query("select m.name from Member m")
    List<String> findAllMemberNames();

    @Query("select new study.springdatajpa.dto.MemberDTO(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTOs();

    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findMembersByName(String name);
    Member findMemberByName(String name);
    Optional<Member> findOptionalMemberByName(String name);
}
