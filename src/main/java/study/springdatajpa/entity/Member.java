package study.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_MEMBER")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "age"})
@NamedQuery(
        name = "Member.findByNameNamedQuery",
        query = "select m from Member m where m.name = :name"
)
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Member(String name, Integer age, Team team) {
        this.name = name;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        this.team.getMembers().add(this);
    }
}
