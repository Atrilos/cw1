package pro.sky;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.sky.entity.Employee;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeBookTest {

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private EmployeeBook testBook;

    @BeforeEach
    void testConstructor() throws NoSuchFieldException, IllegalAccessException {
        Field field = Employee.class.getDeclaredField("idCounter");
        field.setAccessible(true);
        field.set(null, 0L);
        Employee[] employees = new Employee[10];
        employees[0] = new Employee("John", "1", 123d);
        employees[1] = new Employee("Helen", "2", 150d);
        employees[2] = new Employee("Jim", "3", 180d);
        employees[4] = new Employee("Ann", "5", 210d);
        employees[5] = new Employee("Rob", "3", 190d);
        employees[6] = new Employee("Kim", "1", 140d);
        employees[7] = new Employee("Jun", "2", 119.5d);
        employees[8] = new Employee("Jeff", "5", 300.56d);
        employees[9] = new Employee("Burg", "4", 200d);
        testBook = new EmployeeBook(employees);
        out.reset();
    }

    @Test
    void testToString() {
        assertDoesNotThrow(() -> testBook.toString());
    }

    @Test
    void testToString1() {
        assertDoesNotThrow(() -> testBook.toString("1"));
        assertDoesNotThrow(() -> testBook.toString("2"));
        assertDoesNotThrow(() -> testBook.toString("3"));
        assertDoesNotThrow(() -> testBook.toString("4"));
        assertDoesNotThrow(() -> testBook.toString("5"));
        assertEquals("[]", testBook.toString("6"));
    }

    @Test
    void totalSalary() {
        assertEquals(1613.06d, testBook.totalSalary());
        EmployeeBook tmpBook = new EmployeeBook();
        assertEquals(0d, tmpBook.totalSalary());
    }

    @Test
    void testTotalSalary() {
        assertEquals(263d, testBook.totalSalary("1"));
        assertEquals(269.5d, testBook.totalSalary("2"));
        assertEquals(370d, testBook.totalSalary("3"));
        assertEquals(200d, testBook.totalSalary("4"));
        assertEquals(510.56d, testBook.totalSalary("5"));
        assertThrows(IllegalArgumentException.class, () -> testBook.totalSalary("6"));
        EmployeeBook tmpBook = new EmployeeBook();
        assertEquals(0d, tmpBook.totalSalary("1"));
    }

    @Test
    void findMinSalaryEmployee() {
        Employee poorBob = new Employee("Poor Bob", "1", 10d);
        testBook.add(poorBob);
        assertEquals(poorBob, testBook.findMinSalaryEmployee());
    }

    @Test
    void testFindMinSalaryEmployee() {
        Employee poorBob = new Employee("Poor Bob", "1", 10d);
        testBook.add(poorBob);
        assertEquals(poorBob, testBook.findMinSalaryEmployee("1"));
        assertNotEquals(poorBob, testBook.findMinSalaryEmployee("2"));
    }

    @Test
    void findMaxSalaryEmployee() {
        Employee richBob = new Employee("Rich Bob", "1", 10_000d);
        testBook.add(richBob);
        assertEquals(richBob, testBook.findMaxSalaryEmployee());
    }

    @Test
    void testFindMaxSalaryEmployee() {
        Employee richBob = new Employee("Rich Bob", "1", 10_000d);
        testBook.add(richBob);
        assertEquals(richBob, testBook.findMaxSalaryEmployee("1"));
        assertNotEquals(richBob, testBook.findMaxSalaryEmployee("2"));
    }

    @Test
    void averageSalary() {
        double expected = (123 + 150 + 180 + 210 + 190 + 140 + 119.5 + 300.56 + 200) / 9;
        assertEquals(expected, testBook.averageSalary());
        EmployeeBook tmpBook = new EmployeeBook();
        assertEquals(0, tmpBook.averageSalary());
    }

    @Test
    void testAverageSalary() {
        double expected1 = (123d + 140) / 2;
        double expected2 = (150 + 119.5) / 2;
        double expected3 = (180d + 190) / 2;
        double expected4 = 200d;
        double expected5 = (210 + 300.56) / 2;
        assertEquals(expected1, testBook.averageSalary("1"));
        assertEquals(expected2, testBook.averageSalary("2"));
        assertEquals(expected3, testBook.averageSalary("3"));
        assertEquals(expected4, testBook.averageSalary("4"));
        assertEquals(expected5, testBook.averageSalary("5"));
        EmployeeBook tmpBook = new EmployeeBook();
        assertEquals(0, tmpBook.averageSalary("2"));
    }

    @Test
    void printAllNames() {
        String expected = "John, Helen, Jim, Ann, Rob, Kim, Jun, Jeff, Burg\r\n";
        testBook.printAllNames();
        assertEquals(expected, out.toString());
    }

    @Test
    void indexSalary() {
        assertThrows(IllegalArgumentException.class, () -> testBook.indexSalary(0));
        double before = testBook.averageSalary();
        testBook.indexSalary(50);
        double after = testBook.averageSalary();
        assertEquals(before * 1.5d, after);
    }

    @Test
    void testIndexSalary() {
        assertThrows(IllegalArgumentException.class, () -> testBook.indexSalary("0", 50));
        assertThrows(IllegalArgumentException.class, () -> testBook.indexSalary("1", 0));
        double before = testBook.averageSalary("1");
        testBook.indexSalary("1", 50);
        double after = testBook.averageSalary("1");
        assertEquals(before * 1.5d, after);
    }

    @Test
    void printLessThanStrictlySalaryEmployees() {
        testBook.printLessThanStrictlySalaryEmployees(150);
        String expected = """
                Employee(id=1, name=John, salary=123,00)
                Employee(id=6, name=Kim, salary=140,00)
                Employee(id=7, name=Jun, salary=119,50)\r
                """;
        assertEquals(expected, out.toString());
    }

    @Test
    void printMoreThanSalaryEmployees() {
        testBook.printMoreThanSalaryEmployees(150);
        String expected = """
                Employee(id=2, name=Helen, salary=150,00)
                Employee(id=3, name=Jim, salary=180,00)
                Employee(id=4, name=Ann, salary=210,00)
                Employee(id=5, name=Rob, salary=190,00)
                Employee(id=8, name=Jeff, salary=300,56)
                Employee(id=9, name=Burg, salary=200,00)\r
                """;
        assertEquals(expected, out.toString());
    }

    @Test
    void add() {
        assertFalse(testBook.add(null));
        Employee dave = new Employee("Dave", "2", 100d);
        assertTrue(testBook.add(dave));
        String expected = "[Employee(id=1, name=John, division=1, salary=123,00), Employee(id=2, name=Helen, " +
                "division=2, salary=150,00), Employee(id=3, name=Jim, division=3, salary=180,00), Employee(id=10," +
                " name=Dave, division=2, salary=100,00), Employee(id=4, name=Ann, division=5, salary=210,00), " +
                "Employee(id=5, name=Rob, division=3, salary=190,00), Employee(id=6, name=Kim, division=1, " +
                "salary=140,00), Employee(id=7, name=Jun, division=2, salary=119,50), Employee(id=8, name=Jeff, " +
                "division=5, salary=300,56), Employee(id=9, name=Burg, division=4, salary=200,00)]\r\n";
        System.out.println(testBook);
        assertEquals(expected, out.toString());
        out.reset();
        assertFalse(testBook.add(dave));
        assertEquals("The array is already full\r\n", out.toString());
    }

    @Test
    void remove() {
        assertTrue(testBook.remove(1L));
        assertEquals("Employee(id=1, name=John, division=1, salary=123,00) successfully deleted.\r\n",
                out.toString());
        out.reset();
        assertFalse(testBook.remove("John"));
        assertEquals("Employee not found\r\n", out.toString());
        Employee john1 = new Employee("John", "1", 100d);
        Employee john2 = new Employee("John", "5", 200d);
        testBook.add(john1);
        testBook.add(john2);
        assertThrows(IllegalArgumentException.class, () -> testBook.remove("John"));
    }

    @Test
    void printEmployeesByDivisions() {
        String expected = """
                1:
                \r
                Employee(id=1, name=John, salary=123,00)\r
                Employee(id=6, name=Kim, salary=140,00)\r
                \r
                2:
                \r
                Employee(id=2, name=Helen, salary=150,00)\r
                Employee(id=7, name=Jun, salary=119,50)\r
                \r
                3:
                \r
                Employee(id=3, name=Jim, salary=180,00)\r
                Employee(id=5, name=Rob, salary=190,00)\r
                \r
                4:
                \r
                Employee(id=9, name=Burg, salary=200,00)\r
                \r
                5:
                \r
                Employee(id=4, name=Ann, salary=210,00)\r
                Employee(id=8, name=Jeff, salary=300,56)\r
                \r
                """;
        testBook.printEmployeesByDivisions();
        assertEquals(expected, out.toString());
    }

    @BeforeAll
    static void setStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    static void restoreInitialStreams() {
        System.setOut(originalOut);
    }
}