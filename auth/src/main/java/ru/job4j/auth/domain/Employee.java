package ru.job4j.auth.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private long individualTaxpayerNumber;
    private Timestamp dateOfHiring;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Person> persons;

    public static Employee of(String name, String surname, long individualTaxpayerNumber, Timestamp dateOfHiring, List<Person> persons) {
        Employee employee = new Employee();
        employee.name = name;
        employee.surname = surname;
        employee.individualTaxpayerNumber = individualTaxpayerNumber;
        employee.dateOfHiring = new Timestamp(System.currentTimeMillis());
        employee.persons = new ArrayList<>();
        return employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getIndividualTaxpayerNumber() {
        return individualTaxpayerNumber;
    }

    public void setIndividualTaxpayerNumber(long individualTaxpayerNumber) {
        this.individualTaxpayerNumber = individualTaxpayerNumber;
    }

    public Timestamp getDateOfHiring() {
        return dateOfHiring;
    }

    public void setDateOfHiring(Timestamp dateOfHiring) {
        this.dateOfHiring = dateOfHiring;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
