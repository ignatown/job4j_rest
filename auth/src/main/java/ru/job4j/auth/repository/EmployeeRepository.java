package ru.job4j.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.domain.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query("select distinct e from Employee as e left join fetch e.persons")
    Iterable<Employee> findAll();
}
