package pro.sky.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Data class representing employee.<p>
 * Contains information about employee name, division(department), monthly salary.
 */
@Setter
@Getter
@EqualsAndHashCode
public class Employee {

    private static Long idCounter = 0L;

    /**
     * Primary key for Employee class. Sequential generation through idCounter.
     */
    private final Long id;
    private String name;
    private String division;
    private Double salary;

    public Employee(String name, String division, Double salary) {
        id = ++idCounter;
        this.name = name;
        this.division = division;
        this.salary = salary;
    }

    public Employee(Employee employee) {
        id = employee.id;
        name = employee.name;
        division = employee.division;
        salary = employee.salary;
    }

    @Override
    public String toString() {
        return "Employee(id=" + getId() + ", name=" + getName()
                + ", division=" + getDivision()
                + ", salary=" + String.format("%.2f", getSalary()) + ")";
    }

    public String toStringWithoutDivision() {
        return "Employee(id=" + getId() + ", name=" + getName()
                + ", salary=" + String.format("%.2f", getSalary()) + ")";
    }
}
