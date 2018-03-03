package futures.exercise;

import lambda.data.Employee;
import lambda.data.Person;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.*;

import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable", "ConstantConditions", "UnusedAssignment"})
public class Exercise1 {

    @Test
    public void vanillaFutureExample() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(4);

        test(() -> {
            // TODO использовать Executors.newFixedThreadPool(4), Future<T> и метод getEmployee: Person -> Employee
            Future<Employee> employeeFuture = service.submit(() -> getEmployee(getPerson("Дмитрий", "Сашков")));
            Employee result = employeeFuture.get();

            return result;
        });
    }

    @Test
    public void completableFutureExample() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(4);

        test(() -> {
            // TODO использовать CompletableFuture<T> и метод getEmployeeInFuture: Person -> CompletableFuture<Employee>
            CompletableFuture<Employee> employeeFuture = getEmployeeInFuture(getPerson("Дмитрий", "Сашков"));
            employeeFuture.join();
            Employee result = employeeFuture.get();

            return result;
        });
    }

    private static void test(Callable<Employee> task) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(("Дмитрий Сашков" + lineSeparator()).getBytes());

        Employee actual = performWithCustomSystemIn(task, input);

        assertEquals(new Employee(new Person("Дмитрий", "Сашков", 24), Collections.emptyList()), actual);
    }

    private static <T> T performWithCustomSystemIn(Callable<T> task, InputStream input) throws Exception {
        InputStream original = System.in;
        try {
            System.setIn(input);
            return task.call();
        } finally {
            System.setIn(original);
        }
    }

    @SneakyThrows
    private static String getPersonNameAndSurnameFromUser() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.readLine();
        }
    }

    @SneakyThrows
    private static Person getPerson(String name, String surname) {
        Person person;
        // For example load from another service
        TimeUnit.SECONDS.sleep(2);
        return person = new Person(name, surname, 24);
    }

    // TODO использовать в vanillaFutureExample
    @SneakyThrows
    private static Employee getEmployee(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);
        // For example load from another service
        return employee = new Employee(person, Collections.emptyList());
    }

    // TODO использовать в completableFutureExample
    @SneakyThrows
    private static CompletableFuture<Employee> getEmployeeInFuture(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);
        // For example load from another service

        CompletableFuture<Employee> res = new CompletableFuture<Employee>();
        res.complete(new Employee(person, Collections.emptyList()));
        return res;
    }
}
