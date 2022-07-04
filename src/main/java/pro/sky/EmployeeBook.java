package pro.sky;

import lombok.NoArgsConstructor;
import pro.sky.entity.Employee;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hybrid class. Service class for Employee and repository with appropriate functionality.<p>
 * Contains various methods for modifying Employee, as well as modifying and searching within the repository.
 */
@NoArgsConstructor
public class EmployeeBook {

    private final Scanner SC = new Scanner(System.in);
    private final int MAX_SIZE = 10;
    private final Employee[] employees = new Employee[MAX_SIZE];

    /**
     * Constructs an EmployeeBook with the same content as specified array
     *
     * @param e the array whose content is being assigned
     * @throws NullPointerException if specified array is null
     */
    public EmployeeBook(Employee[] e) {
        if (e == null)
            throw new NullPointerException("Parameter can't be null");
        System.arraycopy(e, 0, employees, 0, MAX_SIZE);
    }

    /**
     * Overridden toString() for class
     *
     * @return String with all employees from repository(array),
     * including empty as <i>null</i>
     */
    @Override
    public String toString() {
        return Arrays.toString(employees);
    }

    /**
     * toString() with employees from specified division
     *
     * @param division specified division
     * @return String with employees of specified <i>division</i> or <b><i>[]</i></b> if none present
     */
    public String toString(String division) {
        Employee[] processEmployees = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division))
                .toArray(Employee[]::new);

        return Arrays.toString(processEmployees);
    }

    /**
     * Calculates total salary among all employees
     * @return total salary or <i>0</i> if repository is empty
     */
    public double totalSalary() {
        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .mapToDouble(Employee::getSalary).sum();
    }

    /**
     * Calculates total salary among specified division employees
     * @param division specified division
     * @return total salary or <i>0</i> if repository is empty or no employees in division
     */
    public double totalSalary(String division) {
        validateDivision(division);

        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division))
                .mapToDouble(Employee::getSalary).sum();
    }

    /**
     * @return employee with minimum salary among all employees
     * @throws NoSuchElementException if repository(array) is empty
     */
    public Employee findMinSalaryEmployee() {
        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();
    }

    /**
     * @param division specified division
     * @return employee with minimum salary in the <i>division</i>
     * @throws NoSuchElementException if no employees in repository belongs to <b><i>division</i></b>
     */
    public Employee findMinSalaryEmployee(String division) {
        validateDivision(division);
        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division))
                .min(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();
    }

    /**
     * @return employee with the highest salary among all employees
     * @throws NoSuchElementException if repository(array) is empty
     */
    public Employee findMaxSalaryEmployee() {
        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();
    }

    /**
     * @param division specified division
     * @return employee with the highest salary in the <i>division</i>
     * @throws NoSuchElementException if no employees in repository belongs to <b><i>division</i></b>
     */
    public Employee findMaxSalaryEmployee(String division) {
        validateDivision(division);
        return Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division))
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();
    }

    /**
     * Calculates average salary among all employees.
     * @return average salary or <i>0</i> if repository is empty
     */
    public double averageSalary() {
        long count = Arrays.stream(employees).filter(Objects::nonNull).count();
        if (count == 0) {
            return 0;
        }
        return totalSalary() / (double) count;
    }

    /**
     * Calculates average salary among specified division employees.
     * @param division specified division
     * @return average salary or <i>0</i> if repository is empty or no employees in division
     */
    public double averageSalary(String division) {
        long count = Arrays.stream(employees).filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division)).count();
        if (count == 0) {
            return 0;
        }
        return totalSalary(division) / (double) count;
    }

    /**
     * Method for printing all non-null entry name fields of repository.
     */
    public void printAllNames() {
        String s = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .map(Employee::getName)
                .collect(Collectors.joining(", "));
        System.out.println(s);
    }

    /**
     * Method for indexing salary of each employee, mutates <i>this</i>
     *
     * @param percent percent for indexing salary
     */
    public void indexSalary(double percent) {
        validatePercent(percent);
        double adj = percent / 100;

        for (Employee employee : employees)
            if (employee != null)
                employee.setSalary(employee.getSalary() * (1 + adj));
    }

    /**
     * Method for indexing salary of each employee in the <i>division</i>, mutates <i>this</i>
     *
     * @param division specified division
     * @param percent  percent for indexing salary
     */
    public void indexSalary(String division, double percent) {
        validateDivision(division);
        validatePercent(percent);

        double adj = percent / 100;
        Employee[] filteredEmployees = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(s -> s.getDivision().equals(division))
                .toArray(Employee[]::new);

        for (Employee employee : filteredEmployees)
            employee.setSalary(employee.getSalary() * (1 + adj));
    }

    private void validatePercent(double percent) {
        if (percent <= 0)
            throw new IllegalArgumentException("Percent can't be equal to zero or negative");
    }

    private void validateDivision(String division) {
        if (!division.matches("^[12345]$"))
            throw new IllegalArgumentException("Specified division does not exist");
    }

    /**
     * Method prints employees with less than <i>bound</i> salary or "No such employees".
     * @param bound specified edge case
     */
    public void printLessThanStrictlySalaryEmployees(double bound) {
        String string = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(e -> e.getSalary() < bound)
                .map(Employee::toStringWithoutDivision)
                .collect(Collectors.joining("\n"));
        if (string.equals(""))
            System.out.println("No such employees");
        else
            System.out.println(string);
    }

    /**
     * Method prints employees with more than or equal <i>bound</i> salary or "No such employees".
     * @param bound specified edge case
     */
    public void printMoreThanSalaryEmployees(double bound) {
        String string = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .filter(e -> e.getSalary() >= bound)
                .map(Employee::toStringWithoutDivision)
                .collect(Collectors.joining("\n"));
        if (string.equals(""))
            System.out.println("No such employees");
        else
            System.out.println(string);
    }

    /**
     * Adds new <i>employee</i>, mutates <i>this</i>.
     * If method accepts null then do nothing.
     *
     * @param employee employee to add
     * @return true if employee was added, false if repository is full or method argument is null
     */
    public boolean add(Employee employee) {
        if (employee == null)
            return false;
        for (int i = 0; i < MAX_SIZE; i++) {
            if (employees[i] == null) {
                employees[i] = new Employee(employee);
                return true;
            }
        }
        System.out.println("The array is already full");
        return false;
    }

    private int findEmployee(String name) {
        int modCount = 0;
        int foundIndex = -1;

        for (int i = 0; i < MAX_SIZE; i++) {
            if (employees[i] != null)
                if (employees[i].getName().equalsIgnoreCase(name)) {
                    foundIndex = i;
                    ++modCount;
                }
        }
        if (modCount > 1)
            throw new IllegalArgumentException("Multiple employees with same credentials prohibited");
        return foundIndex;
    }

    private int findEmployee(Long id) {
        int foundIndex = -1;

        for (int i = 0; i < MAX_SIZE; i++) {
            if (employees[i] != null)
                if (employees[i].getId().equals(id))
                    foundIndex = i;
        }
        return foundIndex;
    }

    /**
     * Removes employee by id
     * @param id id of employee
     * @return true if remove successful, false - otherwise
     */
    public boolean remove(long id) {
        int foundIndex;
        foundIndex = findEmployee(id);
        return removeByIndex(foundIndex);
    }

    /**
     * Removes employee by name
     * @param name name of employee
     * @return true if remove successful, false - otherwise
     */
    public boolean remove(String name) {
        int foundIndex;
        foundIndex = findEmployee(name);
        return removeByIndex(foundIndex);
    }

    private boolean removeByIndex(int index) {
        String foundEmployee;

        if (index != -1) {
            foundEmployee = employees[index].toString();
            employees[index] = null;
            System.out.println(foundEmployee + " successfully deleted.");
            return true;
        } else {
            System.out.println("Employee not found");
            return false;
        }
    }

    /**
     * Method for modifying employee.
     * @param name name of employee to find
     * @throws NoSuchElementException if employee by name not found
     * @throws IllegalArgumentException if user input not equals to 1 or 2
     */
    public void modify(String name) {
        int foundIndex = findEmployee(name);
        if (foundIndex == -1)
            throw new NoSuchElementException("No such employee");

        System.out.println("Choose action:\n 1. Change salary.\n 2. Change division.\n");
        switch (SC.nextLine().trim()) {
            case "1" -> modifySalary(foundIndex);
            case "2" -> modifyDivision(foundIndex);
            default -> throw new IllegalStateException("Unexpected value");
        }
    }

    private void modifySalary(int index) {
        System.out.println("Enter new salary value:\n");
        employees[index].setSalary(Double.valueOf(SC.nextLine()));
    }

    private void modifyDivision(int index) {
        System.out.println("Enter new division (1-5):\n");
        String newDivision = SC.nextLine().trim();
        validateDivision(newDivision);
        employees[index].setDivision(newDivision);
    }

    /**
     * Prints in console all employees divided by division or "Array is empty" if repository is empty.
     */
    public void printEmployeesByDivisions() {
        Set<String> divisions = Arrays.stream(employees)
                .filter(Objects::nonNull)
                .map(Employee::getDivision).collect(Collectors.toSet());
        if (divisions.isEmpty()) {
            System.out.println("Array is empty");
            return;
        }
        for (String division : divisions) {
            System.out.println(division + ":\n");
            Arrays.stream(employees)
                    .filter(Objects::nonNull)
                    .filter(e -> e.getDivision().equals(division))
                    .forEach(e -> System.out.println(e.toStringWithoutDivision()));
            System.out.println();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeBook that = (EmployeeBook) o;
        return Arrays.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(employees);
    }
}
