package spliterators.exercise.exercise4;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class Exercise4 {
    private boolean isParallel;

    @Parameterized.Parameters
    public static List<Boolean> data() {
        return Arrays.asList(true, false);
    }

    @Test
    public void createCombinedStreamUsingAlternatingSpliterator() {
        final Stream<Integer> A = Arrays.stream(new Integer[]{1, 2, 3, 4, 5});
        final Stream<Integer> B = Arrays.stream(new Integer[]{6, 7, 8, 9, 0});
        final List<Integer> expected = Arrays.asList(1, 6, 2, 7, 3, 8, 4, 9, 5, 0);

        assertEquals(expected, StreamSupport.stream(
                AlternatingSpliterator.combine(A, B),
                isParallel)
                .collect(Collectors.toList()));
    }
}
