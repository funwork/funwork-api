package io.funwork.api.organization.domain.support.command;

import io.funwork.api.organization.domain.SecurityGrade;
import lombok.Data;

@Data
public class PersonCommand {

    private Long personId;
    private String email;
    private String passwd;
    private String position;
    private SecurityGrade securityGrade;
    private Long deptId;
}
