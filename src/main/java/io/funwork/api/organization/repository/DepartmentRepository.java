package io.funwork.api.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.funwork.api.organization.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
