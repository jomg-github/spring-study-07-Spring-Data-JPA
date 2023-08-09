package study.springdatajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;


    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findByNameNamedQuery(String name) {
        return em.createNamedQuery("Member.findByNameNamedQuery", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Member> findByNameContainingAndAgeGreaterThan(String name, Integer age) {
        return em.createQuery("select m from Member m where m.name like :name and m.age > :age", Member.class)
                .setParameter("name", "%" + name + "%")
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findPagedMembersByAge(Integer age, Integer offset, Integer limit) {
        return em.createQuery("select m from Member m where m.age >= :age order by m.name desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long countByAge(Integer age) {
        return em.createQuery("select count(m) from Member m where m.age >= :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public Integer bulkAgePlus1() {
        return em.createQuery("update Member m set m.age = m.age + 1")
                .executeUpdate();
    }
}
