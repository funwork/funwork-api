package io.funwork.api.organization.domain.support.dto;

import io.funwork.api.organization.domain.Department;
import lombok.Data;

@Data
public class DepartmentInfoDto {

    private Long id;
    private String name;
    private Long parentId;
    private String useYn;

    public DepartmentInfoDto(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.parentId = (department.getParentDept() != null) ? department.getParentDept().getId() : null;
        this.useYn = department.getUseYn();
    }

}
