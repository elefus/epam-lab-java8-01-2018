package streams.part2.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.part3.example.Example1;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class Exercise2 {

    @Data
    @AllArgsConstructor
    private class Tuple<A, B> {
        private final A first;
        private final B second;
    }

    @Data
    @AllArgsConstructor
    private static class Triple<A, B, C> {
        private final A first;
        private final B second;
        private final C third;
    }

    /**
     * Преобразовать список сотрудников в отображение [компания -> множество людей, когда-либо работавших в этой компании].
     *
     * Входные данные:
     * [
     *     {
     *         {Иван Мельников 30},
     *         [
     *             {2, dev, "EPAM"},
     *             {1, dev, "google"}
     *         ]
     *     },
     *     {
     *         {Александр Дементьев 28},
     *         [
     *             {2, tester, "EPAM"},
     *             {1, dev, "EPAM"},
     *             {1, dev, "google"}
     *         ]
     *     },
     *     {
     *         {Дмитрий Осинов 40},
     *         [
     *             {3, QA, "yandex"},
     *             {1, QA, "EPAM"},
     *             {1, dev, "mail.ru"}
     *         ]
     *     },
     *     {
     *         {Анна Светличная 21},
     *         [
     *             {1, tester, "T-Systems"}
     *         ]
     *     }
     * ]
     *
     * Выходные данные:
     * [
     *    "EPAM" -> [
     *       {Иван Мельников 30},
     *       {Александр Дементьев 28},
     *       {Дмитрий Осинов 40}
     *    ],
     *    "google" -> [
     *       {Иван Мельников 30},
     *       {Александр Дементьев 28}
     *    ],
     *    "yandex" -> [ {Дмитрий Осинов 40} ]
     *    "mail.ru" -> [ {Дмитрий Осинов 40} ]
     *    "T-Systems" -> [ {Анна Светличная 21} ]
     * ]
     */
    @Test
    public void employersStuffList() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Set<Person>> result = employees.stream()
            .flatMap(employee -> employee.getJobHistory().stream()
                .map(JobHistoryEntry::getEmployer)
                .map(employer -> new Tuple<>(employee.getPerson(), employer)))
            .collect(
                groupingBy(
                    Tuple::getSecond,
                    mapping(Tuple::getFirst, toSet())));

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
     *
     * Пример.
     *
     * Входные данные:
     * [
     *     {
     *         {Иван Мельников 30},
     *         [
     *             {2, dev, "EPAM"},
     *             {1, dev, "google"}
     *         ]
     *     },
     *     {
     *         {Александр Дементьев 28},
     *         [
     *             {2, tester, "EPAM"},
     *             {1, dev, "EPAM"},
     *             {1, dev, "google"}
     *         ]
     *     },
     *     {
     *         {Дмитрий Осинов 40},
     *         [
     *             {3, QA, "yandex"},
     *             {1, QA, "EPAM"},
     *             {1, dev, "mail.ru"}
     *         ]
     *     },
     *     {
     *         {Анна Светличная 21},
     *         [
     *             {1, tester, "T-Systems"}
     *         ]
     *     }
     * ]
     *
     * Выходные данные:
     * [
     *    "EPAM" -> [
     *       {Иван Мельников 30},
     *       {Александр Дементьев 28}
     *    ],
     *    "yandex" -> [ {Дмитрий Осинов 40} ]
     *    "T-Systems" -> [ {Анна Светличная 21} ]
     * ]
     */
    @Test
    public void indexByFirstEmployer() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Set<Person>> result = employees.stream()
            .map(e -> new Tuple<>(e.getPerson(), e.getJobHistory().iterator().next().getEmployer()))
            .collect(groupingBy(Tuple::getSecond, mapping(Tuple::getFirst, toSet())));

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
    @Test
    public void greatestExperiencePerEmployer() {
        List<Employee> employees = Example1.getEmployees();

        Map<String, Person> collect = employees.stream()
            .flatMap(e -> e.getJobHistory().stream()
                .collect(toMap(
                    JobHistoryEntry::getEmployer,
                    JobHistoryEntry::getDuration,
                    Integer::sum
                ))
                .entrySet().stream()
                .map(es -> new Triple<>(e.getPerson(), es.getKey(), es.getValue())))
            .collect(toMap(Triple::getSecond, Function.identity(), (a, b) -> a.getThird() > b.getThird() ? a : b))
            .entrySet().stream()
            .collect(toMap(
                Map.Entry::getKey,
                e -> e.getValue().getFirst(),
                (a, b) -> a
            ));

        Map<String, Person> expected = new HashMap<>();
        expected.put("EPAM", employees.get(4).getPerson());
        expected.put("google", employees.get(1).getPerson());
        expected.put("yandex", employees.get(2).getPerson());
        expected.put("mail.ru", employees.get(2).getPerson());
        expected.put("T-Systems", employees.get(5).getPerson());
        assertEquals(expected, collect);
    }
}