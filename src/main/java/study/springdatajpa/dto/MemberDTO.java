package study.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String name;
    private String teamName;

    public MemberDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
