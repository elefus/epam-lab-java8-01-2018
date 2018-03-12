package spliterators.exercise.exercise4;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Exercise4 {
    private Collection<Integer> A = Arrays.asList(1, 2, 3, 4, 5);
    private Collection<Integer> B = Arrays.asList(6, 7, 8, 9, 0);
    private Collection<Integer> expected = Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0);


    @Test
    public void testThatWeCanCreateNotParallelCombinedStream() {
        AlternatingSpliterator<Integer> spliterator =
                AlternatingSpliterator.combine(A.stream(), B.stream());

        Stream<Integer> stream = StreamSupport.stream(spliterator, false);

        assertThat(expected, is(stream.collect(Collectors.toList())));
    }

    @Test
    public void testThatWeCanCreateParallelCombinedStream() {
        Stream<Integer> stream = StreamSupport.stream(
                AlternatingSpliterator.combine(A.stream(), B.stream()), true);

        assertThat(expected, is(stream.collect(Collectors.toList())));
    }
}