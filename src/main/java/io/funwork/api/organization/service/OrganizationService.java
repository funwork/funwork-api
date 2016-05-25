package io.funwork.api.organization.service;

import io.funwork.api.organization.domain.Department;
import io.funwork.api.organization.domain.DepartmentPerson;
import io.funwork.api.organization.domain.Person;
import io.funwork.api.organization.domain.support.command.PersonCommand;
import io.funwork.api.organization.domain.support.dto.OrganizationTreeDto;
import io.funwork.api.organization.exception.NotFoundDepartment;
import io.funwork.api.organization.exception.NotFoundPerson;
import io.funwork.api.organization.repository.DepartmentPersonRepository;
import io.funwork.api.organization.repository.PersonRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DepartmentPersonRepository departmentPersonRepository;

    @Transactional
    public Person savePerson(PersonCommand personCommand) {

        Person person = Person.createPerson(personCommand);
        person = personRepository.save(person);

        //부서등록
        if (personCommand.getDeptId() != null) {

            Department department = new Department();
            department.setId(personCommand.getDeptId());

            DepartmentPerson departmentPerson = saveDepartmentPerson(person, department);
            person.addDepartment(departmentPerson);
        }

        return person;
    }

    @Transactional
    public OrganizationTreeDto getTreeByPerson(Long id) {

        Person person = personRepository.findOne(id);
        if(person == null) throw new NotFoundPerson();
        Department department = getDepartmentByPerson(person);
        if(department == null) throw new NotFoundDepartment();
        OrganizationTreeDto tree = makeTree(department, null);
        return tree;
    }

    private OrganizationTreeDto makeTree(Department department, OrganizationTreeDto childTree) {
        OrganizationTreeDto treeDto = new OrganizationTreeDto(department);
        treeDto.setChildren(childs(department, childTree));
        Department parentDept = department.getParentDept();
        if (parentDept != null) return makeTree(parentDept, treeDto);
        return treeDto;
    }

    private List<OrganizationTreeDto> childs(Department department, OrganizationTreeDto tree) {
        List<OrganizationTreeDto> childs = new ArrayList<>();

        department.getDepartmentPersons()
                .stream()
                .filter(departmentPerson -> departmentPerson.getPerson() != null)
                .forEach(departmentPerson -> childs.add(new OrganizationTreeDto(departmentPerson.getPerson(), department.getId())));

        if (tree != null) childs.add(tree);
        return childs;
    }

    private Department getDepartmentByPerson(Person person) {
        if (person.getDepartmentPersons().size() > 0) {
            return person.getDepartmentPersons().get(0).getDepartment();
        }

        return null;
    }

    private DepartmentPerson saveDepartmentPerson(Person person, Department department) {
        DepartmentPerson departmentPerson = new DepartmentPerson(person, department);
        return departmentPersonRepository.save(departmentPerson);
    }
}
