package pro.sky.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Data class representing employee.<p>
 * Contains information about employee name, division(department), monthly salary.
 */
@Setter
@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) && Objects.equals(name, employee.name)
                && Objects.equals(division, employee.division) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, division, salary);
    }
}
