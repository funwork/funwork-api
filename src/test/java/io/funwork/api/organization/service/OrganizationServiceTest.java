package io.funwork.api.organization.service;

import io.funwork.api.organization.domain.Department;
import io.funwork.api.organization.domain.DepartmentPerson;
import io.funwork.api.organization.domain.Person;
import io.funwork.api.organization.domain.SecurityGrade;
import io.funwork.api.organization.domain.support.command.PersonCommand;
import io.funwork.api.organization.domain.support.dto.OrganizationTreeDto;
import io.funwork.api.organization.exception.NotFoundDepartment;
import io.funwork.api.organization.exception.NotFoundPerson;
import io.funwork.api.organization.fixture.DepartmentFixture;
import io.funwork.api.organization.fixture.PersonFixture;
import io.funwork.api.organization.repository.DepartmentPersonRepository;
import io.funwork.api.organization.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "test")
public class OrganizationServiceTest {

    @InjectMocks
    OrganizationService organizationService = new OrganizationService();

    @Mock
    PersonRepository personRepository;

    @Mock
    DepartmentPersonRepository departmentPersonRepository;

    Person givenPerson;
    Person expectPerson;
    Person treeExpectPerson;

    @Before
    public void setUp() {
        PersonFixture personFixture = createPersonFixture();
        givenPerson = personFixture.build();
        expectPerson = personFixture.withId(1L).build();
        treeExpectPerson = createTreePerson();
    }

    @Test
    public void test_mock_organization_service() throws Exception {
        assertNotNull(organizationService);
    }

    @Test
    public void test_add_person() throws Exception {
        //given
        PersonCommand personCommand = createPersonCommandFixture();
        when(personRepository.save(givenPerson)).thenReturn(expectPerson);

        //when
        Person savePerson = organizationService.savePerson(personCommand);

        //then
        verify(personRepository).save(givenPerson);
        assertThat(savePerson.getId(), is(expectPerson.getId()));
    }

    @Test
    public void test_add_person_and_belong_dept() throws Exception {

        //given
        PersonCommand personCommand = createPersonCommandFixture();
        personCommand.setDeptId(1L);
        when(personRepository.save(givenPerson)).thenReturn(expectPerson);

        Department department = new Department();
        department.setId(1L);

        DepartmentPerson expectDepartmentPerson = new DepartmentPerson();
        expectDepartmentPerson.setId(1L);
        expectDepartmentPerson.setDepartment(department);
        expectDepartmentPerson.setPerson(expectPerson);

        when(departmentPersonRepository.save(any(DepartmentPerson.class))).thenReturn(expectDepartmentPerson);

        //when
        Person savePerson = organizationService.savePerson(personCommand);

        //then
        verify(departmentPersonRepository).save(any(DepartmentPerson.class));
        assertThat(savePerson.getId(), is(expectPerson.getId()));
        assertThat(savePerson.getDepartmentPersons().size(), is(1));
    }

    @Test
    public void test_get_tree_by_person() throws Exception {

        //given
        when(personRepository.findOne(1L)).thenReturn(treeExpectPerson);

        //when
        OrganizationTreeDto treeDto = organizationService.getTreeByPerson(1L);

        //then
        log.info(treeDto.toString());
        verify(personRepository).findOne(1L);
        assertThat(treeDto.getTitle(), is("테스트1"));
        assertThat(treeDto.getChildren().get(0).getTitle(), is("테스트1-1"));
        assertThat(treeDto.getChildren().get(0).getChildren().get(0).getTitle(), is("테스트1-1사원"));
    }

    @Test(expected = NotFoundDepartment.class)
    public void test_get_tree_by_person_for_exception() throws Exception {
        //given
        when(personRepository.findOne(1L)).thenReturn(expectPerson);

        //when
        OrganizationTreeDto treeDto = organizationService.getTreeByPerson(1L);

        //then
        verify(personRepository).findOne(1L);
    }

    @Test(expected = NotFoundPerson.class)
    public void test_get_tree_by_person_but_person_is_null() throws Exception {
        //given
        when(personRepository.findOne(1L)).thenReturn(null);

        //when
        OrganizationTreeDto treeDto = organizationService.getTreeByPerson(1L);

        //then
        verify(personRepository).findOne(1L);
    }

    private PersonCommand createPersonCommandFixture() {
        PersonCommand personCommand = new PersonCommand();
        personCommand.setEmail(givenPerson.getEmail());
        personCommand.setPasswd(givenPerson.getPasswd());
        personCommand.setPosition(givenPerson.getPosition());
        personCommand.setSecurityGrade(givenPerson.getSecurityGrade());
        return personCommand;
    }

    private PersonFixture createPersonFixture() {
        return PersonFixture.anPerson()
                .withEmail("test1234@funwork.io")
                .withPasswd("test1234!")
                .withPosition("사원")
                .withSecurityGrade(SecurityGrade.NORMAL);
    }

    private Person createTreePerson() {
        Department cDepartment = createDepartmentFixture();
        PersonFixture fixture = createPersonFixture();
        Person person = fixture.withId(1L).withName("테스트1-1사원").build();
        List<DepartmentPerson> departmentPersonList = createDepartmentPersonsFixture(cDepartment, person);
        cDepartment.setDepartmentPersons(departmentPersonList);
        person.setDepartmentPersons(departmentPersonList);
        return person;
    }

    private List<DepartmentPerson> createDepartmentPersonsFixture(Department cDepartment, Person person) {
        DepartmentPerson departmentPerson = new DepartmentPerson();
        departmentPerson.setId(1L);
        departmentPerson.setDepartment(cDepartment);
        departmentPerson.setPerson(person);

        List<DepartmentPerson> departmentPersonList = new ArrayList<>();
        departmentPersonList.add(departmentPerson);
        return departmentPersonList;
    }

    private Department createDepartmentFixture() {
        DepartmentFixture departmentFixture = DepartmentFixture.anDepartment()
                .withUseYn("Y");

        Department pDepartment = departmentFixture
                .withId(1L)
                .withName("테스트1")
                .build();

        Department cDepartment = departmentFixture
                .withId(2L)
                .withName("테스트1-1")
                .withParentDept(pDepartment)
                .build();

        Set<Department> childDepts = new HashSet<>();
        childDepts.add(cDepartment);

        pDepartment.setChildDept(childDepts);
        return cDepartment;
    }
}