package study.springdatajpa.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.dto.MemberDTO;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
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

    Page<Member> findPagedMembersByAgeGreaterThanEqual(Integer age, Pageable pageable);
    Slice<Member> findSlicedMembersByAgeGreaterThanEqual(Integer age, Pageable pageable);
    List<Member> findMembersByAgeGreaterThanEqual(Integer age, Pageable pageable);

    @Modifying // 있어야 executeUpdate 실행, 아니면 에러 발생
//    @Modifying(clearAutomatically = true) // 쿼리 실행 이후 영속성 컨텍스트 비워주는 옵션
    @Query("update Member m set m.age = m.age + 1")
    Integer bulkAgePlus1();

    @Query("select m from Member m join fetch m.team")
    List<Member> findAllFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findAllEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    @Query("select m from Member m where m.id = :id")
    Member findByIdReadOnly(@Param("id") Long id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.name = :name")
    List<Member> findAllByNameApplyingLock(@Param("name") String name);
}
