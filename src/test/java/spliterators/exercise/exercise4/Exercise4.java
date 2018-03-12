package spliterators.exercise.exercise4;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Exercise4 {

    private Stream<Integer> A = Stream.of(1, 2, 3, 4, 5);
    private Stream<Integer> B = Stream.of(6, 7, 8, 9, 0);

    private Stream<Integer> A1 = Stream.of(1, 2, 3, 4, 5, 1, 2, 3, 4, 5);
    private Stream<Integer> B1 = Stream.of(6, 7, 8, 9, 0, 6, 7, 8, 9, 0);

    @Test
    public void streamsEqualLengthCombinedNotParallel() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(A, B);
        assertThat(Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0), is(StreamSupport.stream(alternatingSpliterator, false).collect(Collectors.toList())));
    }

    @Test
    public void streamsNotEqualLengthCombinedNotParallelFirst() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(A1, B);
        assertThat(Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0, 1, 2, 3, 4, 5), is(StreamSupport.stream(alternatingSpliterator, false).collect(Collectors.toList())));
    }

    @Test
    public void streamsNotEqualLengthCombinedNotParallelSecond() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(B1, A);
        assertThat(Arrays.asList(6, 1, 7, 2, 8, 3, 9, 4, 0, 5, 6, 7, 8, 9, 0), is(StreamSupport.stream(alternatingSpliterator, false).collect(Collectors.toList())));
    }

    @Test
    public void streamsEqualLengthCombinedParallel() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(A, B);
        assertThat(Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0), is(StreamSupport.stream(alternatingSpliterator, true).collect(Collectors.toList())));
    }

    @Test
    public void streamsNotEqualLengthCombinedParallelFirst() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(A1, B);
        assertThat(Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0, 1, 2, 3, 4, 5), is(StreamSupport.stream(alternatingSpliterator, true).collect(Collectors.toList())));
    }

    @Test
    public void streamsNotEqualLengthCombinedParallelSecond() {
        AlternatingSpliterator<Integer> alternatingSpliterator = AlternatingSpliterator.combine(B1, A);
        assertThat(Arrays.asList(6, 1, 7, 2, 8, 3, 9, 4, 0, 5, 6, 7, 8, 9, 0), is(StreamSupport.stream(alternatingSpliterator, true).collect(Collectors.toList())));
    }
}
