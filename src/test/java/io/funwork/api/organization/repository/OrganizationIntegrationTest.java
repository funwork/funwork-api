package io.funwork.api.organization.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import io.funwork.FunworkApiApplicationTests;
import io.funwork.api.organization.domain.Department;
import io.funwork.api.organization.domain.DepartmentPerson;
import io.funwork.api.organization.domain.Person;
import io.funwork.api.organization.domain.SecurityGrade;
import io.funwork.api.organization.fixture.DepartmentFixture;
import io.funwork.api.organization.fixture.PersonFixture;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FunworkApiApplicationTests.class)
@ActiveProfiles(profiles = "test")
public class OrganizationIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DepartmentPersonRepository departmentPersonRepository;

    private Department parentDept;
    private Department childDept;

    private Person person;
    private Person person2;

    @Before
    public void setUp() {
        parentDept = createParentDept();
        childDept = createChildDept();
        person = createPersonFixture()
            .withEmail("test@funwork.com")
            .withName("테스트")
            .build();
        person2 = createPersonFixture()
            .withEmail("test2@funwork.com")
            .withName("테스트2")
            .build();
    }

    @Test
    public void test_add_parent_department() {
        //given
        //when
        Department saveDepartment = saveParentDept();

        //then
        assertThat(saveDepartment.getName(), is(parentDept.getName()));
    }

    @Test
    public void test_add_child_department() {
        //given
        Department saveParentDept = saveParentDept();

        //when
        Department saveChildDept = saveChildDeptAndParent(saveParentDept);

        //then
        assertThat(saveChildDept.getName(), is(childDept.getName()));
        assertThat(saveParentDept.getChildDept().size(), is(1));
    }

    @Test
    public void test_add_person() {
        //given
        //when
        Person savePerson = savePerson();

        //then
        assertThat(savePerson.getName(), is(savePerson.getName()));
    }

    @Test
    public void test_add_person_dept() {
        //given
        Department saveParentDept = saveParentDept();
        Department saveChildDept = saveChildDeptAndParent(saveParentDept);
        Person savePerson = savePerson();
        Person savePerson2 = personRepository.save(person2);

        //when
        saveDepartmentPerson(saveChildDept, savePerson);
        saveDepartmentPerson(saveChildDept, savePerson2);

        //then
        assertThat(getDepartmentPerson(saveChildDept).size(), is(2));
        assertThat(getDepartmentPerson(saveChildDept).get(0).getPerson().getName(), is("테스트"));
    }

    @Test
    @Transactional
    public void test_get_dept_person_by_person() {

        //given
        //when
        Person newPerson = personRepository.findOne(1L);

        //then
        assertThat(newPerson.getDepartmentPersons().size(), is(1));
    }

    @Test
    @Transactional
    public void test_get_dept_by_person() {

        //given
        Person newPerson = personRepository.findOne(1L);
        Department newDepartment = new Department();
        List<DepartmentPerson> departmentPersons = newPerson.getDepartmentPersons();

        //when
        if(departmentPersons.size() > 0)
            newDepartment = departmentPersons.get(0).getDepartment();

        //then
        assertThat(newDepartment.getId(), is(2L));
    }


    private Department saveParentDept() {
        return departmentRepository.save(parentDept);
    }

    private Department saveChildDeptAndParent(Department saveParentDept) {
        Department saveChildDept = departmentRepository.save(childDept);
        saveChildDept.addParentDept(saveParentDept);
        saveChildDept = departmentRepository.save(saveChildDept);
        return saveChildDept;
    }


    private Person savePerson() {
        return personRepository.save(person);
    }


    private List<DepartmentPerson> getDepartmentPerson(Department saveChildDept) {
        return departmentRepository
            .findOne(saveChildDept.getId()).getDepartmentPersons();
    }

    private DepartmentPerson saveDepartmentPerson(Department saveChildDept, Person savePerson) {
        DepartmentPerson departmentPerson = new DepartmentPerson();
        departmentPerson.setDepartment(saveChildDept);
        departmentPerson.setPerson(savePerson);
        departmentPersonRepository.save(departmentPerson);
        return departmentPerson;
    }

    private Department createParentDept() {
        return DepartmentFixture.anDepartment()
            .withName("테스트1")
            .withUseYn("Y")
            .build();
    }

    private Department createChildDept() {
        return DepartmentFixture.anDepartment()
            .withName("테스트1-1")
            .withUseYn("Y")
            .build();
    }

    private PersonFixture createPersonFixture() {
        return PersonFixture.anPerson()
            .withPasswd("test1234!")
            .withPosition("사원")
            .withSecurityGrade(SecurityGrade.NORMAL);
    }
}