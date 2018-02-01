package streams.part1.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.part3.example.Example1;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"ConstantConditions", "unused"})
public class Exercise2 {

    @Test
    public void calcAverageAgeOfEmployees() {
        List<Employee> employees = Example1.getEmployees();

        Double expected = employees.stream()
                                    .map(Employee::getPerson)
                                    .mapToInt(Person::getAge)
                                    .average()
                                    .orElseThrow(IllegalStateException::new);


        assertEquals(33.66, expected, 0.1);
    }

    @Test
    public void findPersonWithLongestFullName() {
        List<Employee> employees = Example1.getEmployees();

        Person expected = employees.stream()
                                    .map(Employee::getPerson)
                                    .max(Comparator.comparing(Person::getFullName, Comparator.comparingInt(String::length)))
                                    .orElseThrow(IllegalStateException::new);

        assertEquals(expected, employees.get(1).getPerson());
    }

    @Test
    public void findEmployeeWithMaximumDurationAtOnePosition() {
        List<Employee> employees = Example1.getEmployees();

        Employee expected = employees.stream()
                                    .max(Comparator.comparingInt(employee -> employee.getJobHistory()
                                                                                    .stream()
                                                                                    .mapToInt(JobHistoryEntry::getDuration)
                                                                                    .max()
                                                                                    .orElse(0)))
                                    .orElseThrow(IllegalStateException::new);

        assertEquals(expected, employees.get(4));
    }
    
    @Test
    public void calcTotalSalaryWithCoefficientWorkExperience() {
        List<Employee> employees = Example1.getEmployees();

        Double expected = employees.stream()
                                    .mapToDouble(employee -> employee.getJobHistory().get(employee.getJobHistory().size() - 1).getDuration() > 3 ? 90000 : 75000)
                                    .sum();
        assertEquals(465000.0, expected, 0.001);
    }
}