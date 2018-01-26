package lambda.data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable.
 */
public class Employee {

    public static void main(String... books) {
        StringBuilder sb = new StringBuilder();
        for (String book : books)
            sb.insert(sb.indexOf("c"), book);
        System.out.println(sb);
    }

    private final Person person;
    private final List<JobHistoryEntry> jobHistory;

    public Employee(Person person, List<JobHistoryEntry> jobHistory) {
        this.person = person;
        this.jobHistory = jobHistory;
    }

    public Person getPerson() {
        return person;
    }

    public List<JobHistoryEntry> getJobHistory() {
        return new ArrayList<>(jobHistory);
    }

    @Override
    public String toString() {
        return "Employee@" + hashCode() + " {"
             + "person=" + person + ", "
             + "jobHistory=" + jobHistory + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;

        return Objects.equals(person, employee.person)
            && Objects.equals(jobHistory, employee.jobHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, jobHistory);
    }
}
