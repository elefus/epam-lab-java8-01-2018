package streams.part2.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.part3.example.Example1;
import org.junit.Test;
import streams.part2.example.data.PersonEmployerDuration;
import streams.part2.example.data.PersonEmployerPair;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class Exercise2 {
    /**
     * Преобразовать список сотрудников в отображение [компания -> множество людей, когда-либо работавших в этой компании].
     * <p>
     * Входные данные:
     * [
     * {
     * {Иван Мельников 30},
     * [
     * {2, dev, "EPAM"},
     * {1, dev, "google"}
     * ]
     * },
     * {
     * {Александр Дементьев 28},
     * [
     * {2, tester, "EPAM"},
     * {1, dev, "EPAM"},
     * {1, dev, "google"}
     * ]
     * },
     * {
     * {Дмитрий Осинов 40},
     * [
     * {3, QA, "yandex"},
     * {1, QA, "EPAM"},
     * {1, dev, "mail.ru"}
     * ]
     * },
     * {
     * {Анна Светличная 21},
     * [
     * {1, tester, "T-Systems"}
     * ]
     * }
     * ]
     * <p>
     * Выходные данные:
     * [
     * "EPAM" -> [
     * {Иван Мельников 30},
     * {Александр Дементьев 28},
     * {Дмитрий Осинов 40}
     * ],
     * "google" -> [
     * {Иван Мельников 30},
     * {Александр Дементьев 28}
     * ],
     * "yandex" -> [ {Дмитрий Осинов 40} ]
     * "mail.ru" -> [ {Дмитрий Осинов 40} ]
     * "T-Systems" -> [ {Анна Светличная 21} ]
     * ]
     */
    private static Stream<PersonEmployerPair> getPersonEmployerPairStream(Employee employee) {
        return employee.getJobHistory()
                .stream()
                .map(JobHistoryEntry::getEmployer)
                .map(employer -> new PersonEmployerPair(employee.getPerson(), employer));
    }

    @Test
    public void employersStuffList() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Set<Person>> result = employees.stream()
                .parallel()
                .flatMap(Exercise2::getPersonEmployerPairStream)
                .collect(groupingBy(PersonEmployerPair::getEmployer, mapping(PersonEmployerPair::getPerson, toSet())));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected.put("yandex", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected.put("mail.ru", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected.put("EPAM", new HashSet<>(Arrays.asList(
                employees.get(0).getPerson(),
                employees.get(1).getPerson(),
                employees.get(4).getPerson(),
                employees.get(5).getPerson()
        )));
        expected.put("google", new HashSet<>(Arrays.asList(
                employees.get(0).getPerson(),
                employees.get(1).getPerson()
        )));
        expected.put("T-Systems", new HashSet<>(Arrays.asList(
                employees.get(3).getPerson(),
                employees.get(5).getPerson()
        )));
        assertEquals(expected, result);
    }

    /**
     * Преобразовать список сотрудников в отображение [компания -> множество людей, начавших свою карьеру в этой компании].
     * <p>
     * Пример.
     * <p>
     * Входные данные:
     * [
     * {
     * {Иван Мельников 30},
     * [
     * {2, dev, "EPAM"},
     * {1, dev, "google"}
     * ]
     * },
     * {
     * {Александр Дементьев 28},
     * [
     * {2, tester, "EPAM"},
     * {1, dev, "EPAM"},
     * {1, dev, "google"}
     * ]
     * },
     * {
     * {Дмитрий Осинов 40},
     * [
     * {3, QA, "yandex"},
     * {1, QA, "EPAM"},
     * {1, dev, "mail.ru"}
     * ]
     * },
     * {
     * {Анна Светличная 21},
     * [
     * {1, tester, "T-Systems"}
     * ]
     * }
     * ]
     * <p>
     * Выходные данные:
     * [
     * "EPAM" -> [
     * {Иван Мельников 30},
     * {Александр Дементьев 28}
     * ],
     * "yandex" -> [ {Дмитрий Осинов 40} ]
     * "T-Systems" -> [ {Анна Светличная 21} ]
     * ]
     */
    private static Stream<PersonEmployerPair> getFirstCompanyPersonPairStream(Employee employee) {
        return Arrays.asList(employee.getJobHistory().get(0))
                             .stream()
                             .map(jhe -> new PersonEmployerPair(employee.getPerson(), jhe.getEmployer()));
        }

    @Test
    public void indexByFirstEmployer() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Set<Person>> result = employees.stream()
                .flatMap(Exercise2::getFirstCompanyPersonPairStream)
                .collect(groupingBy(PersonEmployerPair::getEmployer, mapping(PersonEmployerPair::getPerson, toSet())));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected.put("yandex", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected.put("EPAM", new HashSet<>(Arrays.asList(
                employees.get(0).getPerson(),
                employees.get(1).getPerson(),
                employees.get(4).getPerson()
        )));
        expected.put("T-Systems", new HashSet<>(Arrays.asList(
                employees.get(3).getPerson(),
                employees.get(5).getPerson()
        )));
        assertEquals(expected, result);
    }

    /**
     * Преобразовать список сотрудников в отображение [компания -> сотрудник, суммарно проработавший в ней наибольшее время].
     * Гарантируется, что такой сотрудник будет один.
     */

    private static Stream<PersonEmployerDuration> getPersonEmployerDurationStream(Employee employee) {
        return employee.getJobHistory()
                .stream()
                .collect(groupingBy(JobHistoryEntry::getEmployer, summingInt(JobHistoryEntry::getDuration)))
                .entrySet()
                .stream()
                .map(entry -> new PersonEmployerDuration(employee.getPerson(), entry.getKey(), entry.getValue()));
    }

    @Test
    public void greatestExperiencePerEmployer() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Person> collect = employees.stream()
                .flatMap(Exercise2::getPersonEmployerDurationStream)
                .collect(groupingBy(PersonEmployerDuration::getEmployer, maxBy(Comparator.comparingInt(PersonEmployerDuration::getDuration))))
                .entrySet()
                .stream()
                .map(entry -> new PersonEmployerPair(entry.getValue().get().getPerson(), entry.getKey()))
                .collect(toMap(PersonEmployerPair::getEmployer, PersonEmployerPair::getPerson));

        Map<String, Person> expected = new HashMap<>();
        expected.put("EPAM", employees.get(4).getPerson());
        expected.put("google", employees.get(1).getPerson());
        expected.put("yandex", employees.get(2).getPerson());
        expected.put("mail.ru", employees.get(2).getPerson());
        expected.put("T-Systems", employees.get(5).getPerson());
        assertEquals(expected, collect);
    }
}