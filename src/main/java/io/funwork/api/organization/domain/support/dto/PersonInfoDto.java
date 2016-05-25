package io.funwork.api.organization.domain.support.dto;

import io.funwork.api.organization.domain.Person;
import io.funwork.api.organization.domain.SecurityGrade;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonInfoDto {

    private Long id;
    private String email;
    private String name;
    private String position;
    private SecurityGrade securityGrade;

    public PersonInfoDto(Person person) {
        this.setId(person.getId());
        this.setEmail(person.getEmail());
        this.setName(person.getName());
        this.setPosition(person.getPosition());
        this.setSecurityGrade(person.getSecurityGrade());
    }
}
