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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable", "ConstantConditions", "UnusedAssignment"})
public class Exercise1 {

    @Test
    public void vanillaFutureExample() throws Exception {
        test(() -> {
            final ExecutorService executorService = Executors.newFixedThreadPool(4);
            final Future<String> fullNameFuture = executorService.submit(Exercise1::getPersonNameAndSurnameFromUser);
            final String[] fullName = fullNameFuture.get().split(" ");
            final Future<Person> personFuture = executorService.submit(() -> getPerson(fullName[0], fullName[1]));
            return executorService.submit(() -> getEmployee(personFuture.get())).get();
        });
    }

    @Test
    public void completableFutureExample() throws Exception {
        test(() ->
            CompletableFuture.supplyAsync(Exercise1::getPersonNameAndSurnameFromUser)
                .thenApplyAsync(s -> {
                    final String[] fullName = s.split(" ");
                    return getPerson(fullName[0], fullName[1]);
                })
                .thenComposeAsync(Exercise1::getEmployeeInFuture)
                .join()
        );
    }

    private static void test(Callable<Employee> task) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(("Дмитрий Сашков" + lineSeparator()).getBytes());

        Employee actual = performWithCustomSystemIn(task, input);

        assertEquals(new Employee(new Person("Дмитрий", "Сашков", 25), Collections.emptyList()), actual);
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
        return person = new Person(name, surname, 25);
    }

    @SneakyThrows
    private static Employee getEmployee(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);
        // For example load from another service
        return employee = new Employee(person, Collections.emptyList());
    }

    @SneakyThrows
    private static CompletableFuture<Employee> getEmployeeInFuture(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);
        // For example load from another service
        return CompletableFuture.completedFuture(getEmployee(person));
    }
}
