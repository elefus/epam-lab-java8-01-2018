package streams.part2.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.part3.example.Example1;
import org.junit.Test;
import streams.part2.example.data.PersonCompanyDuration;
import streams.part2.example.data.PersonCompanyPair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class Exercise2 {

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
                                                   .parallel()
                                                   .flatMap(Exercise2::getPersonCompanyPairStream)
                                                   .reduce(new HashMap<>(), Exercise2::addToMap, Exercise2::mergeMaps);

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

    private static Stream<PersonCompanyPair> getPersonCompanyPairStream(Employee employee) {
        return employee.getJobHistory()
                       .stream()
                       .map(JobHistoryEntry::getEmployer)
                       .map(company -> new PersonCompanyPair(employee.getPerson(), company));
    }

    private static HashMap<String, Set<Person>> addToMap(Map<String, Set<Person>> map, PersonCompanyPair pair) {
        HashMap<String, Set<Person>> res = new HashMap<>(map);
        res.compute(pair.getCompany(), (company, persons) -> {
            persons = persons == null ? new HashSet<>() : persons;
            persons.add(pair.getPerson());
            return persons;
        });
        return res;
    }

    private static HashMap<String, Set<Person>> mergeMaps(HashMap<String, Set<Person>> left, HashMap<String, Set<Person>> right) {
        HashMap<String, Set<Person>> res = new HashMap<>(left);
        right.forEach((company, persons) -> res.merge(company, persons,
                (leftPersons, rightPersons) -> {
                    leftPersons.addAll(rightPersons);
                    return leftPersons;
                }));
        return res;
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
                .map( e -> new PersonCompanyPair(e.getPerson(), e.getJobHistory().get(0).getEmployer()))
                                                   .reduce(new HashMap<>(), Exercise2::addToMap, Exercise2::mergeMaps);

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
                                               .parallel()
                                               .flatMap(employee -> employee.getJobHistory()
                                                                            .stream()
                                                                            .collect(toMap(JobHistoryEntry::getEmployer,
                                                                                    entry -> new PersonCompanyDuration(employee.getPerson(), entry.getEmployer(), entry.getDuration()),
                                                                                    (pair1, pair2) -> new PersonCompanyDuration(pair1.getPerson(), pair1.getCompany(),
                                                                                            pair1.getDuration() + pair2.getDuration())
                                                                                    )
                                                                            )
                                                                            .entrySet()
                                                                            .stream()
                                               )
                                               .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (pair1, pair2) -> pair1.getDuration() > pair2.getDuration() ? pair1 : pair2))
                                               .entrySet()
                                               .stream()
                                               .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getPerson()));


        Map<String, Person> expected = new HashMap<>();
        expected.put("EPAM", employees.get(4).getPerson());
        expected.put("google", employees.get(1).getPerson());
        expected.put("yandex", employees.get(2).getPerson());
        expected.put("mail.ru", employees.get(2).getPerson());
        expected.put("T-Systems", employees.get(5).getPerson());
        assertEquals(expected, collect);
    }
}