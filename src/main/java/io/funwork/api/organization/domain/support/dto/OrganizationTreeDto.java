package io.funwork.api.organization.domain.support.dto;

import java.util.ArrayList;
import java.util.List;

import io.funwork.api.organization.domain.Department;
import io.funwork.api.organization.domain.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrganizationTreeDto {

    public static final String DEPT_TYPE = "DEPT";
    public static final String USER_TYPE = "USER";
    private String title;
    private String key;
    private String type;
    private Object info;
    private List<OrganizationTreeDto> children = new ArrayList<>();


    public OrganizationTreeDto(Department department) {
        this.setTitle(department.getName());
        this.setKey(getTreeKey(department));
        this.setType(DEPT_TYPE);
        this.setInfo(new DepartmentInfoDto(department));
    }

    public OrganizationTreeDto(Person person, Long deptId) {
        this.setTitle(person.getName());
        this.setKey(getTreeKey(person, deptId));
        this.setType(USER_TYPE);
        this.setInfo(new PersonInfoDto(person));
    }

    private String getTreeKey(Department department) {
        Department parentDept = department.getParentDept();
        return (parentDept != null) ? DEPT_TYPE + parentDept.getId() + "-" + DEPT_TYPE + department.getId() : DEPT_TYPE + department.getId();
    }

    private String getTreeKey(Person person, Long deptId) {
        return DEPT_TYPE + deptId + "-" + USER_TYPE + person.getId();
    }
}
