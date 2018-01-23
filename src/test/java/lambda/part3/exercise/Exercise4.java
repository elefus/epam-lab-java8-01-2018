package lambda.part3.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.part3.example.Example1;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Exercise4 {

    @SuppressWarnings("WeakerAccess")
    private static class LazyCollectionHelper<T, R> {

        private final List<T> src;
        private final Function<T, List<R>> f;

        private LazyCollectionHelper(List<T> src, Function<T, List<R>> f) {
            this.src = src;
            this.f = f;
        }

        public static <T> LazyCollectionHelper<T, T> from(List<T> list) {
            return new LazyCollectionHelper<>(list, Collections::singletonList);
        }

        private <A, B> Function<List<A>, List<B>> helper(Function<A, List<B>> f) {
            return list -> {
                final List<B> newList = new ArrayList<>();
                list.forEach(f.andThen(newList::addAll)::apply);
                return newList;
            };
        }

        public <U> LazyCollectionHelper<T, U> flatMap(Function<R, List<U>> flatMapping) {
            return new LazyCollectionHelper<>(src, f.andThen(helper(flatMapping)));
        }

        public <U> LazyCollectionHelper<T, U> map(Function<R, U> mapping) {
            return new LazyCollectionHelper<>(src, f.andThen(helper(mapping.andThen(Collections::singletonList))));
        }

        public List<R> force() {
            return helper(f).apply(src);
        }
    }

    @Test
    public void mapEmployeesToCodesOfLetterTheirPositionsUsingLazyFlatMapHelper() {
        List<Employee> employees = Example1.getEmployees();

        List<Integer> codes = LazyCollectionHelper.from(employees)
            .flatMap(Employee::getJobHistory)
            .map(JobHistoryEntry::getPosition)
            .flatMap(s -> s.chars().boxed().collect(Collectors.toList()))
            .force();
        assertEquals(calcCodes("dev", "dev", "tester", "dev", "dev", "QA", "QA", "dev", "tester", "tester", "QA", "QA", "QA", "dev"), codes);
    }

    private static List<Integer> calcCodes(String... strings) {
        List<Integer> codes = new ArrayList<>();
        for (String string : strings) {
            for (char letter : string.toCharArray()) {
                codes.add((int) letter);
            }
        }
        return codes;
    }
}
