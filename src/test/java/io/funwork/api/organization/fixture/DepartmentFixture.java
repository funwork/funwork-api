package io.funwork.api.organization.fixture;

import io.funwork.api.organization.domain.Department;

import java.util.HashSet;
import java.util.Set;

public class DepartmentFixture {

    private Long id;
    private String name;
    private Department parentDept;
    private String useYn;
    private Set<Department> childDept = new HashSet<>();

    public static DepartmentFixture anDepartment() {
        return new DepartmentFixture();
    }

    public DepartmentFixture withName(String name) {
        this.name = name;
        return this;
    }

    public DepartmentFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public DepartmentFixture withParentDept(Department parentDept) {
        this.parentDept = parentDept;
        return this;
    }

    public DepartmentFixture withUseYn(String useYn) {
        this.useYn = useYn;
        return this;
    }

    public DepartmentFixture withChildDept(Set<Department> childDept) {
        this.childDept = childDept;
        return this;
    }

    public Department build() {
        Department department = new Department();
        department.setId(this.id);
        department.setName(this.name);
        department.setParentDept(this.parentDept);
        department.setUseYn(this.useYn);
        if(childDept.size() > 0)
            department.setChildDept(childDept);
        return department;
    }
}
