package lambda.part3.exercise;

import lambda.data.Employee;
import lambda.part3.example.Example1;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Exercise4 {

    private static class LazyCollectionHelper<T, R> {

        private List<T> source;
        private final Function<T, List<R>> remapping;

        private LazyCollectionHelper(List<T> source, Function<T, List<R>> f) {
            this.source = source;
            this.remapping = f;
        }

        public static <T> LazyCollectionHelper<T, T> from(List<T> list) {
            return new LazyCollectionHelper<>(list, Collections::singletonList);
        }

        public <U> LazyCollectionHelper<T, U> flatMap(Function<R, List<U>> flatMapping) {
            return new LazyCollectionHelper<>(source, remapping.andThen( transformUsing(flatMapping)

                    /*res -> {
                List<U> result = new ArrayList<>();
                res.forEach(flatMapping.andThen(result::addAll)::apply);
                return result;
            }
            */));
        }

        public <U> LazyCollectionHelper<T, U> map(Function<R, U> flatMapping) {
            return new LazyCollectionHelper<>(source, remapping.andThen(res -> {
                List<U> result = new ArrayList<>();
                res.forEach(flatMapping.andThen(result::add)::apply);
                return result;
            }));

        }

        public List<R> force() {
            List<R> result = new ArrayList<>();
            /*for (T value : source){
                result.addAll(remapping.apply(value));
            }
            */
            //source.forEach(value -> result.addAll(remapping.apply(value)));
            source.forEach(value -> remapping.andThen(result::addAll));
            return result;
        }

        private <FROM, TO> Function<List<FROM>, List<TO>> transformUsing(Function<FROM, List<TO>> mapper){
            return source -> {
                List<TO> result = new ArrayList<>();
                source.forEach(mapper.andThen(result::addAll)::apply);
                return result;
            };
        }
    }


    @Test
    public void mapEmployeesToCodesOfLetterTheirPositionsUsingLazyFlatMapHelper() {
        List<Employee> employees = Example1.getEmployees();

        List<Integer> codes = null;
        // TODO              LazyCollectionHelper.from(employees)
        // TODO                                  .flatMap(Employee -> JobHistoryEntry)
        // TODO                                  .map(JobHistoryEntry -> String(position))
        // TODO                                  .flatMap(String -> Character(letter))
        // TODO                                  .map(Character -> Integer(code letter)
        // TODO                                  .getMapped();
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
